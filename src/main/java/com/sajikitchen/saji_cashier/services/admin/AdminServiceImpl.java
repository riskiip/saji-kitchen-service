package com.sajikitchen.saji_cashier.services.admin;

import com.sajikitchen.saji_cashier.dto.admin.CashierSalesDto;
import com.sajikitchen.saji_cashier.dto.admin.DailySalesDetailDto;
import com.sajikitchen.saji_cashier.dto.admin.DailySalesDto;
import com.sajikitchen.saji_cashier.dto.admin.DashboardDataDto;
import com.sajikitchen.saji_cashier.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;
    private final ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");

    @Override
    public DashboardDataDto getDashboardData() {
        System.out.println("sampai sini");
        // Tentukan rentang waktu (misal: 30 hari terakhir untuk chart)
        OffsetDateTime todayStart = LocalDate.now(jakartaZone).atStartOfDay().atOffset(jakartaZone.getRules().getOffset(java.time.Instant.now()));
        OffsetDateTime todayEnd = LocalDate.now(jakartaZone).atTime(LocalTime.MAX).atOffset(jakartaZone.getRules().getOffset(java.time.Instant.now()));
        OffsetDateTime last30Days = todayStart.minusDays(30);

        // 1. Panggil query untuk total omzet hari ini
        BigDecimal todayRevenue = orderRepository.findTodayRevenue(todayStart, todayEnd);
        System.out.println("--- today " + todayRevenue);

        // 2. Panggil query untuk chart penjualan harian
        List<DailySalesDto> dailySales = orderRepository.findTotalSalesPerDay(last30Days);

        // 3. Panggil query untuk chart penjualan per kasir
        List<CashierSalesDto> salesByCashier = orderRepository.findTotalSalesByCashier(last30Days);

        // Gabungkan semua data ke dalam satu DTO
        return DashboardDataDto.builder()
                .todayRevenue(todayRevenue)
                .dailySales(dailySales)
                .salesByCashier(salesByCashier)
                .build();
    }

    @Override
    public List<DailySalesDetailDto> getDailySalesDetail(String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date); // Parsing tanggal dari string format "YYYY-MM-DD"
        } catch (DateTimeParseException e) {
            // Jika format tanggal salah, gunakan tanggal hari ini sebagai default
            localDate = LocalDate.now(jakartaZone);
        }

        OffsetDateTime startOfDay = localDate.atStartOfDay().atOffset(jakartaZone.getRules().getOffset(java.time.Instant.now()));
        OffsetDateTime endOfDay = localDate.atTime(LocalTime.MAX).atOffset(jakartaZone.getRules().getOffset(java.time.Instant.now()));

        return orderRepository.findSalesDetailByDate(startOfDay, endOfDay);
    }
}
