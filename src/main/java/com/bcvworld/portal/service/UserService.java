package com.bcvworld.portal.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bcvworld.portal.model.User;
import com.bcvworld.portal.repository.UserManagementRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class UserService {

    @Autowired
    private UserManagementRepository userRepository;

    public Page<User> getAllUsers(int page, int size, String search, String status, String role) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search by name, email, or phone
            if (StringUtils.hasText(search)) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern);
                Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("mobile")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, emailPredicate, phonePredicate));
            }

            // Filter by Status
            if (StringUtils.hasText(status) && !"all".equalsIgnoreCase(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }

            // Filter by Role
            if (StringUtils.hasText(role) && !"all".equalsIgnoreCase(role)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("role")), role.toLowerCase()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable);
    }

    public User updateUserStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setStatus(status);
        return userRepository.save(user);
    }
}