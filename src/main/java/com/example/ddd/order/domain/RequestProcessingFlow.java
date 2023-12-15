package com.example.ddd.order.domain;

public class RequestProcessingFlow {
    // Controller -> App Service -> Domain -> Repository -> DB

    // 사용자 요청 -> Controller
    // MVC 구조라면 Controller 가 사용자의 요청 받아 처리하게 된다.

    // Controller -> App Service
    // 표현 영역은 사용자가 전송한 데이터 형식이 올바른지 검사하고 문제가 없다면 데이터를 이용해서 응용 서비스에 기능 실행을 위임.
    // 이때 표현 영역은 사용자가 전송한 데이터를 응용 서비스가 요구하는 형식으로 변환해서 전달한다.

    // App Service -> Domain -> Repository
    // 기능 구현에 필요한 도메인 객체를 리포지터리에서 가져와 실행하거나 신규 도메인 객체를 생성하여 리포지터리에 저장.
    // 예매하기 , 예매취소 기능을 예로 들면 응용 서비스는 도메인의 상태를 변경하므로 변경 상태가 물리 저장소에 올바르게 반영되도록 트랜잭션을 관리.
}
