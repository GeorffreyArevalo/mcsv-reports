package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.reports.ApprovedReportResponseDTO;
import co.com.crediya.model.ApprovedReport;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReportsMapper {
    ApprovedReportResponseDTO modelToResponse(ApprovedReport report);
}
