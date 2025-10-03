package com.sajikitchen.saji_cashier.services.admin;

import com.sajikitchen.saji_cashier.dto.admin.*;
import com.sajikitchen.saji_cashier.models.*;
import com.sajikitchen.saji_cashier.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final OrderRepository orderRepository;
    private final ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ToppingRepository toppingRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public Product createProduct(ProductRequestDto request) {
        Product newProduct = new Product();
        newProduct.setName(request.getName());
        newProduct.setDescription(request.getDescription());
        newProduct.setImageUrl(request.getImageUrl());
        newProduct.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(UUID productId, ProductRequestDto request) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) {
            existingProduct.setActive(request.getIsActive());
        }
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        product.setActive(false); // Soft delete
        productRepository.save(product);
    }

    @Override
    public ProductVariant createVariant(UUID productId, VariantRequestDto request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ProductVariant newVariant = new ProductVariant();
        newVariant.setProduct(product);
        newVariant.setName(request.getName());
        newVariant.setPrice(request.getPrice());

        return productVariantRepository.save(newVariant);
    }

    @Override
    public ProductVariant updateVariant(UUID variantId, VariantRequestDto request) {
        ProductVariant existingVariant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new EntityNotFoundException("Product Variant not found with id: " + variantId));

        existingVariant.setName(request.getName());
        existingVariant.setPrice(request.getPrice());

        return productVariantRepository.save(existingVariant);
    }

    @Override
    public Topping createTopping(ToppingRequestDto request) {
        Topping newTopping = new Topping();
        newTopping.setName(request.getName());
        newTopping.setPrice(request.getPrice());
        newTopping.setImageUrl(request.getImageUrl());
        newTopping.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        return toppingRepository.save(newTopping);
    }

    @Override
    public Topping updateTopping(UUID toppingId, ToppingRequestDto request) {
        Topping existingTopping = toppingRepository.findById(toppingId)
                .orElseThrow(() -> new EntityNotFoundException("Topping not found with id: " + toppingId));

        existingTopping.setName(request.getName());
        existingTopping.setPrice(request.getPrice());
        existingTopping.setImageUrl(request.getImageUrl());
        if (request.getIsActive() != null) {
            existingTopping.setActive(request.getIsActive());
        }

        return toppingRepository.save(existingTopping);
    }

    @Override
    public List<UserResponseDto> findAllCashiers() {
        return userRepository.findAllByRoleRoleName("CASHIER").stream()
                .map(this::mapUserToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createCashier(CreateUserRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        Role cashierRole = roleRepository.findByRoleName("CASHIER")
                .orElseThrow(() -> new EntityNotFoundException("Role 'CASHIER' not found"));

        User newCashier = new User();
        newCashier.setUsername(request.getUsername());
        newCashier.setPassword(passwordEncoder.encode(request.getPassword())); // Enkripsi password!
        newCashier.setRole(cashierRole);
        newCashier.setActive(true);

        User savedUser = userRepository.save(newCashier);
        return mapUserToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto updateCashier(UUID userId, UpdateUserRequestDto request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (request.getIsActive() != null) {
            existingUser.setActive(request.getIsActive());
        }

        User updatedUser = userRepository.save(existingUser);
        return mapUserToResponseDto(updatedUser);
    }

    private UserResponseDto mapUserToResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .roleName(user.getRole().getRoleName())
                .isActive(user.isActive())
                .build();
    }
}
