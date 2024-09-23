package com.dssiddev.disparowhatsapp.repositories.specification;

import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.Questionario;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Subquery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionarioSpacification {
    public static Specification<Questionario> containsCliente(String cliente) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("cliente"),"%" + cliente +"%");
    }

    public static Specification<Questionario> containsConta(List<Conta> contas) {
        List<Long> ids = contas.stream().map(conta -> conta.getId()).collect(Collectors.toList());

        return (root, query, criteriaBuilder) -> {
            root.get("contaId").in(ids);
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Questionario> containsContaId(Long contaId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("contaId"), contaId);
    }

}
