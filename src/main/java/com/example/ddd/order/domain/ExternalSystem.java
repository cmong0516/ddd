package com.example.ddd.order.domain;

import javax.naming.NoPermissionException;

public class ExternalSystem {

    // 외부 시스템과의 연동 기능도 도메인 서비스가 될수 있다.
    // 설문조사 시스템과 사용자 역할 관리 시스템이 분리되어 있다고 한다면 설문조사 시스템은 설문조사를 생성할때 사용자가 생성 권한을 가진 역할인지 확인하기 위해 역할관리 시스템과 연동해야 한다.
    // 시스템간 연동은 HTTP API 호출로 이루어질수 있지만 설문조사 도메인 입장에서는 사용자가 설문 조사 생성 권한을 가졌는지 확인하는 도메인 로직으로 볼수 있다.

    // 중요한 점은 도메인 로직 관점에서 인터페이스를 작성했다는것.
    public interface ServeyPermissionChecker {
        boolean hasUserCreationPermission(String userId);
    }

    public class CreateSurveyService {
        private SurveyPermissionChecker permissionChecker;

        public Long createSurvey(CreateSurveyRequest request) {
            validate(request);
            if (!permissionChecker.hasUserCreationPermission(request.getRequestorId())) {
                throw new NoPermissionException();
            }
        }
    }

    // ServeyPermissionChecker 인터페이스를 구현한 클래스는 인프라스트럭처 영억에 위치해 연동을 포함한 권한 검사 기능을 구현.




}
