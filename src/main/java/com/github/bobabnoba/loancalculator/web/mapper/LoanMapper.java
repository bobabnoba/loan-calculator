package com.github.bobabnoba.loancalculator.web.mapper;

import com.github.bobabnoba.loancalculator.domain.model.Installment;
import com.github.bobabnoba.loancalculator.web.dto.InstallmentDto;
import com.github.bobabnoba.loancalculator.web.dto.LoanRequestDto;
import com.github.bobabnoba.loancalculator.persistence.entity.InstallmentEntity;
import com.github.bobabnoba.loancalculator.persistence.entity.LoanRequestEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanMapper {

    public LoanRequestEntity toLoanRequestEntity(LoanRequestDto dto) {
        if (dto == null) return null;
        return new LoanRequestEntity(
                dto.amount(),
                dto.annualInterestPercent(),
                dto.termMonths()
        );
    }

    public InstallmentEntity toInstallmentEntity(LoanRequestEntity parent, Installment i) {
        return new InstallmentEntity(
                parent, i.month(), i.interest(), i.principal(), i.payment(), i.balance()
        );
    }

    public InstallmentDto toInstallmentDto(InstallmentEntity entity) {
        if (entity == null) return null;
        return new InstallmentDto(
                entity.getMonthIndex(),
                entity.getInterest(),
                entity.getPrincipal(),
                entity.getPayment(),
                entity.getBalance()
        );
    }

    public List<InstallmentDto> toInstallmentDtos(List<InstallmentEntity> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        List<InstallmentDto> out = new ArrayList<>(entities.size());
        for (InstallmentEntity e : entities) {
            out.add(toInstallmentDto(e));
        }
        return out;
    }

    public List<InstallmentEntity> toInstallmentEntities(LoanRequestEntity parent, List<Installment> items) {
        var out = new ArrayList<InstallmentEntity>(items.size());
        for (var i : items) out.add(toInstallmentEntity(parent, i));
        return out;
    }
}
