package com.example.ddd.order.domain;

import org.springframework.security.access.prepost.PreAuthorize;

public class AuthenticationValidate {
    // 사용자가 어떤 기능을 수행할때 그 기능을 수행할수 있는지 확인하는것이 권한검사 이다.
    // 권한검사 자체는 복잡한 개념이 아니지만 개발하는 시스템마다 권한의 복잡도가 다르다.
    // Spring Security 를 사용해서 유연하고 확장 가능한 구조를 갖고 있는데 유안한 만큼 복잡하다.
    // 권한검사를 수행하는 곳은 표현영역 , 응용 서비스 , 도메인 세군데이다.


    // 인증된 사용자인지 여부를 검사하는데 서블릿 필터가 사용된다.
    // 서블릿 ㅣㅍㄹ터에서 사용자이ㅡ 인증 정보를 생성하고 인증 여부를 검사하여 인증된 사용자면 다음 과정을, 그렇지 않다면 로그인 화면이나 에러 화면을 보여준다.


    public class BlockMemberService {
        private MemberRepository memberRepository;

        @PreAuthorize("hasRole('ADMIN')")
        public void block(String memberId) {
            Member member = memberRepository.findById(memberId);
            if (member == null) {
                throw new NoMemberException();
            }

            member.block();
        }

        // 스프링 시큐리티 에서는 @PreAuthorize 어노테이션을 사용해서 서비스 메서드에 대한 권한 검사를 할수있다.
    }

    // 개별 도메인 객체 단위로 권한 검사를 해야 하는 경우는 구현이 복잡해진다.
    // 예를들어 게시글 삭제는본인과 관리자만 가능하다고 하면 게시글 작성자가 본인인지 확인하려면 게시글 애그리거트를 먼저 로딩해야 한다.
    // 응용 서비스의 메서드 수준에서 권한 검사를 할수 없기 때문에 다음과 같이 직접 권한 검사 로직을 구현해야 한다.

    public class DeleteArticleService {
        public void delete(String userId, Long articleId) {
            Article article = articleRepository.findById(articleId);
            checkArticleExistence(article);
            permissionService.checkDeletePermission(userId, article);
            // permissionService.checkDeletePermission()은 파라미터로 전달받은 사용자 ID 와 게시글을 이용해서 삭제 권한을 가졌는지 검사한다.
            article.markDeleted();
        }
    }


}
