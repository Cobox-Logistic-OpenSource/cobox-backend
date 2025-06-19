package coboxlogistic.startup.backend.fleet.domain.model.queries;

import java.time.LocalDateTime;

/**
 * Query to get mileage records by date range in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record GetMileageRecordsByDateRangeQuery(
    LocalDateTime startDate,
    LocalDateTime endDate
) {
    
    public GetMileageRecordsByDateRangeQuery {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
} 