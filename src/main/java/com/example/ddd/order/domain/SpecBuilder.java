package com.example.ddd.order.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.util.StringUtils;
import org.springframework.util.function.SupplierUtils;

public class SpecBuilder {

    // 스펙을 생성하다 보면 조건에 따라 스펙을 조합해야 할 때가 있다.
    // if 문과 각 스펙을 조합한느 코드가 섞여있어 실수하기 좋고 복잡한 구조를 갖는다.
    // 이것을 보완하기 위해 스펙 빌더를 사용해보자.

    public static <T> Builder<T> builder(Class<T> type) {
        return new Builder<T>();
    }

    public static class Builder<T> {
        private List<Specification<T>> specs = new ArrayList<>();

        public Builder<T> and(Specification<T> specification) {
            specs.add(specification);
            return this;
        }

        public Builder<T> isHasText(String string,  Function<String,Specification<T>> specSupplier) {
            if (StringUtils.hasText(string)) {
                specs.add(specSupplier.apply(string));
            }
            return this;
        }

        public Builder<T> ifTure(Boolean cond, Supplier<Specification<T>> specSupplier) {
            if (cond != null && cond.booleanValue()) {
                specs.add(specSupplier.get());
            }

            return this;
        }

        public Specification<T> toSpec() {
            Specification<T> specification = Specification.where(null);
            for (Specification<T> spec : specs) {
                specification = specification.and(spec);
            }

            return specification;
        }
    }

}
