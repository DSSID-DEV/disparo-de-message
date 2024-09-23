package com.dssiddev.disparowhatsapp.repositories.specification;

import com.dssiddev.disparowhatsapp.models.Contato;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;

public class ContatoSpecification {
    public static Specification<Contato> containsNome(String nome) {
        return (root, query, criteriaBuilder) -> {
                if(isEmpty(nome)) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("nome"),"%" + nome +"%");
        };
    }

    public static Specification<Contato> containsNomeOrCellPhone(String contato) {
        return (root, query, criteriaBuilder) -> {
            if(isEmpty(contato)) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%"+contato.toLowerCase()+"%";
            Predicate predicateNome = criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), likePattern);
            Predicate predicateCellPhone = criteriaBuilder.like(criteriaBuilder.lower(root.get("cellPhone")), likePattern);
            return criteriaBuilder.or(predicateNome, predicateCellPhone);
        };
    }
    public static Specification<Contato> containsCellPhone(String cellPhone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("cellPhone"), "%"+cellPhone+"%");
    }
    private static boolean isEmpty(String contato) {
        return contato == null || contato.isBlank() || contato.isEmpty();
    }
}
