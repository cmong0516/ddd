package com.example.ddd.order.domain;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.User;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public class Validation {
    // 6.5 값 검증
    // 값 검증은 표현영역 , 응용서비스 두곳에서 모두 수행할수 있다.
    // 원칙적으로는 모든 값에 대한 검증은 응용 서비스에서 처리한다.


    // 응용 서비스 에서의 값의 형식 , 로직 에 대한 검증.
    public class JoinService {
        @Transactional
        public void join(JoinRequest joinRequest) {
            // 값의 형식 검사
            checkEmpty(joinRequest.getId(), "id");
            checkEmpty(joinRequest.getName(), "name");
            checkEmpty(joinRequest.getPassword(), "password");

            if (joinRequest.getPassword().equals(joinRequest.getConfirmPassword())) {
                throw new InvalidPropertyException();
            }

            // 값의 로직 검사
            checkDuplicatedId(joinRequest.getId());
        }

        private void checkEmpty(String value, String propertyName) {
            if (value == null || value.isEmpty()) {
                throw new EmptyPropertyException(propertyName);
            }
        }

        private void checkDuplicatedId(String id) {
            int count = memberRepsitory.countsById(id);
            if (count > 0) {
                throw new DuplicateIdException();
            }
        }
    }

    // 포현 영역은 잘못된 값이 존재하면 이를 사용자에게 알려주고 값을 다시 입력받아야 한다.
    // 스프링 MVC 에서는 폼에 입력한 값이 잘못된 경우 에러를 보여주기 위한 용도로 Errors , BindingResult 를 사용.

    @org.springframework.stereotype.Controller
    public class Controller {
        @PostMapping("/member/join")
        public String join(JoinRequest joinRequest, Errors errors) {
            try {
                joinService.join(joinRequest);
                return successView;
            } catch (EmptyPropertyException e) {
                errors.rejectValue(e.getPropertyName(), "invalid");
                return formView;
            } catch (DuplicateIdException e) {
                errors.rejectValue(e.getPropertyName(), "duplicate");
                return formView;
            }
        }
    }
    // 응용 서비스에서 각 값이 유효한지 확인할 목적으로 익셉션을 사용할 때의 문제점은 사용자에게 좋지 않은 경험을 제공한다는 것이다.
    // 사용자는 폼에 값을 입력하고 전송했는데 입력한 값이 잘못되어 다시 폼에 입력해야 할때 한개 항목이 아닌 입력한 모든 항목에 대해 잘못된 부분이 있는지 알고 싶을것이다.
    // 그런데 응용 서비스 에서 값을 검사하는 시점에 첫 값이 올바르지 않아 익셉션이 발생하면 그 아래로 검사를 수행하지 않는다.
    // 이런 사용자 불편을 해소하기 위해 에러 코드를 모아 하나의 익셉션으로 발생시키는 방법이 있다.

    @Transactional
    public OrderNo placeOrder(OrderRequest orderRequest) {
        List<ValidationError> errors = new ArrayList<>();
        if (orderRequest == null) {
            errors.add(VAlidationError.of("empty"));
        } else {
            if (orderRequest.getOrdererMemberId() == null) {
                errors.add(ValidationError.of("ordererMemberId", "empty"));
            }
            if (orderRequest.getOrderProducts() == null) {
                errors.add(ValidationError.of("orderProducts", "empty"));
            }
            if (orderRequest.getOrderProducts().isEmpty()) {
                errors.add(ValidationError.of("orderProducts", "empty"));
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationErrorException(errors);
        }
    }

    // 표현 영역은 응용 서비스가 ValidationErrorException 을 발생시키면 익셉션 에서 에러 목록을 가져와 표현 영역에서 사용할 형태로 변환 처리한다.

    @PostMapping("/orders/order")
    public String order(@ModelAttribute("orderReq") OrderRequest orderRequest, BindingResult bindingResult,
                        ModelMap modelMap) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderRequest.setOrderMemberId(MemberId.of(user.getUsername()));
        try {
            OrderNo orderNo = placeOrderService.placeOrer(orderRequest);
            modelMap.addAttribute("orderNo", orderNo.getNumber());
        } catch (ValidationErrorException e) {
            e.getErrors().forEach(err -> {
                if (err.hasName()) {
                    bindingResult.rejectValue(err.getName(), err.getCode());
                } else {
                    bindingResult.rejectValue(err.getCode());
                }
            });
            populateProductModel(orderRequest, modelMap);
            return "order/confirm";
        }
    }

    // 표현 영역에서 필수 값을 검증하는 방법도 있다.

    @PostMapping("/member/join")
    public String join(JoinRequest joinRequest, Errors errors) {
        if (erros.hasErrors()) {
            return formView;
        }
        try {
            joinService.join(joinRequest);
            return successView;
        } catch (DuplicateIdException exception) {
            errors.rejectValue(exception.getPropertyName(), "duplicate");
            return formView;
        }
    }

    // 스프링 에선 Validator 인터페이스를 별도로 제공하므로 인터페이스를 구현한 검증기를 따로 구현하면 코드를 간결하게 줄일수 있다.
    // 표현 영역에서 필수 값과 값의 형식을 검사하면 실질적으로 응용 서비스는 ID중복 여부와 논리적 오류만 검사하면 된다.

    // 표현영역 : 필수값 , 값의 형식 , 범위 등을 검증한다.
    // 응용 서비스 : 데이터의 존재 유무와 같은 논리적 오류를 검증한다.
}
