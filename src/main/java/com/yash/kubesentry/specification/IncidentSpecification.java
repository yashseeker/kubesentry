package com.yash.kubesentry.specification;
import com.yash.kubesentry.model.entity.Incident;
import com.yash.kubesentry.model.enums.IncidentStatus;
import org.springframework.data.jpa.domain.Specification;
import com.yash.kubesentry.model.enums.Severity;
public class IncidentSpecification {
    public static Specification<Incident> hasStatus(
            IncidentStatus status) {

        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }
    public static Specification<Incident> hasSeverity(
            Severity severity) {

        return (root, query, criteriaBuilder) ->

                severity == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("severity"),
                        severity
                );}
        public static Specification<Incident> hasSearch(String search) {

            return (root, query, criteriaBuilder) -> {

                if (search == null || search.isBlank()) {
                    return null;
                }

                String keyword = "%" + search.toLowerCase() + "%";

                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                keyword
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                keyword
                        )
                );
            };
        }  }




