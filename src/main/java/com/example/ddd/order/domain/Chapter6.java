package com.example.ddd.order.domain;

import jakarta.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

public class Chapter6 {
    // 도메인이 제 기능을 하려면 사용자와 도메인을 연결해 주는 매개체가 필요한데 그 매개체로 응용영역 과 표현영역 이 있다.
    // 표현 영역은 사용자의 요청을 해석한다.
    // 사용자가 요청을 보내면 요청 파라미터를 포함한 HTTP요청을 표현 영역에 전달한다.
    // 표현 영역은 URL , 요청 파라미터 ,쿠키 , 헤더 등의 정보를 가지고 사용자가 실행하고 싶은 기능을 판별하고 그 기능을 제공하는 응용 서비스를 실행한다.
    // 표현영역 : 사용자가 실행하고 싶어하는 기능을 판별
    // 응용 서비스 : 사용자가 원하는 기능을 실행

    // 사용자 -> 표현영역 -> 응용역역 -> 도메인영역

    @PostMapping("member/join")
    public ModelAndView join(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 사용자 요청을 응용 서비스에 맞게 변환
        JoinRequest joinRequest = new JoinRequest(email, password);
        // 변환된 객체를 이용해서 응용 서비스 실행
        joinService.join(joinRequest);

    }

    // 6.2 응용 서비스
    // 위에서 설명한대로 응용 서비스란 사용자와 상호작용 하는 표현영역에서 데이터를 받아 기능을 수행한다.
    // 1. 리포지터리에서 애그리거트를 구한다.
    // 2. 애그리거트의 도메인 기능을 실행한다.
    // 3. 결과를 반환한다.

    public Result doSomeFunc(SomeReq req) {
        // 1. 리포지터리에서 애그리거트를 가져온다? 구한다?
        SomeAgg agg = someAggRepository.findById(req.getId());

        // 2. 애그리거트의 도메인 기능을 실행한다.
        agg.doFunc(req.getValue());

        // 3. 결과를 반환하다.
        return createSuccessResult(agg);

    }


    public Result doSomeCreation(CreateSomeReq req) {
        // 1. 데이터 중복 및 유효성 검사
        validate(req);

        // 2. 애그리거트를 생성
        SomeAgg newAgg = createSome(req);

        // 3. 리포지터리에 애그리거트를 저장한다.
        someAggRepository.save(newAgg);

        // 4. 결과를 반환한다.
        return createSuccessResult(newAgg);
    }

    // *만약 응용 서비스가 복잡하다면 응용 서비스에서 도메인 영역에서 처리해야할 로직을 수행하고 있을 가능성이 높다.
    // *응용 서비스는 트랜잭션 처리도 담당하며 도메인의 상태 변경을 트랜잭션으로 처리해야 한다.
    // *데이터의 검증, 중복검사 등 도메인 내부에서 처리해야할 로직이 응용 서비스에 들어가지 않도록 주의하자.
        // 도메인 로직이 응용 서비스에 출현하면 코드의 응집도가 떨어지며 중복가능성이 높다.


    // 6.3 응용 서비스의 구현

    // 응용 서비스를 구현하려면 크기에 대해 고민해 보아야 한다.
    // 회원 도메인의 응용 서비스를 예로 들면 회원 가입 , 탈퇴 , 암호변경 , 비밀번호 초기화 등 많은 기능이 포함된다.
    // 이때 모든 기능을 한 클래스에 담기 , 구분되는 기능별로 응용 서비스 클래스 나누기 두가지가 가능하다.
    // 1. 한 도메인과 관련된 기능을 한 응용 서비스 클래스에 담기.
        // 모든 기능이 하나의 응용 서비스 클래스에 있으므로 중복제거가 용이하지만 클래스의 크기가 기능만큼 커진다.
    // 2. 각 기능별로 응용 서비스의 클래스 나누기
        // 꼭 필요한 코드들만 클래스에 담겨있어 코드 전체를 이해하기 쉽다.
        // 만약 공통 적용되는 기능들이 존재한다면 별도의 중복 기능들을 모아놓은 클래스를 만들어 사용하자.

    // 응용 서비스를 구현할때 인터페이스가 필요한가 ?
    // 1. 구현체가 여러개라면 인터페이스가 필요하다.
        // 하지만 응용 서비스는 그럴일이 적다.

    // 응용 서비스의 반환값으로 애그리거트 객체를 반환하지 말라
        // 처리는 편리할수 있지만 이렇게 될경우 도메인의 로직 실행을 오로지 응용 서비스 에서만 했었는데 이제 표현영역 에서도 가능해진다.

    // 표현영역에 의존하지 마라.
        // 매개변수로 HttpServletRequest , HttpSession 등을 받지마라
        // 위 두가지는 표현영역과 관련된 타입이다.
        // 응용 서비스에서 표현 영역에 대한 의존이 발생하면 응용 서비스의 단독 테스트가 어렵고 표현 영역이 변경되면 응용 영역도 변경해야한다.

    // 응용서비스의 중요한 역할중 하나는 트랜잭션 처리이다.
        // 회원 가입에 성공했으면 내용을 DB에 저장해야 로그인 기능을 수행할수 있다.
            // 변경된 내용을 DB에 저장해서 올바른 기능 처리가 가능하다.
        // 스프링이 제공하는 트랜잭션 관리 기능을 이용하면 쉽게 트랜잭션 처리가 가능하다. @Transactional


    // 6.4 표현 영역

    // 표현영역의 책임
    // 1. 사욪아가 시스템을 사용할수 있는 흐름(화면)을 제공하고 제어한다.
    // 2. 사용자의 요청을 알맞은 응용 서비스에 전달하고 결과를 사용자에게 전달한다.
    // 3. 사용자의 세션을 관리한다.

    @PostMapping()
//    public String changePassword(HttpServletRequest request, Errors errors) {
    // HTTP 요청 파라미터로부터 자바 객체를 생성하는 기능을 사용해보자,
    public String changePassword(ChangePasswordRequest chPwdRequest , Errors errors) {
//        String curPw = request.getParameter("curPw");
//        String newPw = request.getParameter("newPw");
        String memberId = SecurityContext.getAuthentication().getId();
//        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(memberId, curPw, newPw);
        chPwdRequest.setMemberId(memberId);

        try {
            changePasswordService.changePassword(changePasswordRequest);
            return successView;
        } catch (BadPasswordExcpetion | NoMemberException exception) {
            errors.reject("idPasswordNotMatch");
            return formView;
        }
    }
}
