package com.example.ddd.order.domain;

import java.util.List;

public class Dip {
    // Dependency Inversion Principle
    // 고수준 모듈은 저수준 모듈에 의존해서는 안되며 양쪽 모듈은 추상화에 의존해야 한다.
    // 추상화는 세부 사항에 의존해서는 안된다.

    // 할인을 적용하는 CalculateDiscountService 가 있다고 하면
    // 두개의 저수준 모듈
    // 1. RDBMS 에서 JPA로 구한다.
    // 2. Drools 로 룰을 적용한다.

//    public interface RuleDiscounter {
//        Money applyRules(Customer customer, List<OrderLine> orderLines);
//    }

    // 저수준 모듈을 추상화 의존 하기위해 DroolsRuleDiscounter 는 RuleDiscounter 를 구현.

    // CalculateDiscountService   ->   RuleDiscounter(Interface)
    //                                          ^
    //                                          |
    //                                  DroolsRuleDiscounter


    // RuleDiscounter, CalcuateDiscountService : 고수준 모듈
    // DroolsRuleDiscounter는 고수준의 하위 기능인 RuleDiscounter 를 구현한 저수준 모듈
    // DIP 를 적용하여 저수준 모듈이 고수준 모듈에 의존하게 했다.
    // 고수준 모듈이 저수준 모듈을 사용하려면 고수준 모듈이 저수준 모듈을 의존해야 하는데 반대로 저수준 모듈이 고수준 모듈에 의존한다.
    // -> 의존 역전 원칙.

    // DIP 를 사용하면 구현 교체와 테스트가 편해지는 장점이 생긴다.

//    public class CalculateDiscountService {
//        private CustomerRepository customerRepository;
//        private RuleDiscounter ruleDiscounter;
//
//        public CalculateDiscountService(CustomerRepository customerRepository, RuleDiscounter ruleDiscounter) {
//            this.customerRepository = customerRepository;
//            this.ruleDiscounter = ruleDiscounter;
//        }
//
//        public Money calculateDiscount(List<OrderLine> orderLines, String customerId) {
//            Customer customer = customerRepository.findById(customerId);
//            return ruleDiscounter.applyRules(customer, orderLines);
//        }
//
//        public Customer findCustomer(String customerId) {
//            Customer customer = customerRepository.findById(customerId);
//            if (customer == null) {
//                throw new NoCustomerException();
//            }
//
//            return customer;
//        }
//    }

    // 위 service 에 대한 테스트를 작성하고자 한다면 직접 의존 했을시 customerRepository , ruleDiscounter 구현체가 있어야 한다.
    // 하지만 DIP 를 적용하여

//    CustomerRepository customerRepository = mock(CustomerRepository.class);
//    when(customerRepository.findById("Test")).thenReturn(null);
//
//    이런식으로 테스트를 진행할수 있다.

    // 단 DIP를 단순히 인터페이스와 구현 클래스의 분리 라고 착각해선 안된다.
    // DIP란 고수준 모듈이 저수준 모듈에 의존하지 않도록 하기 위한것 이다.

}
