package com.agregator.Agregator.Controllers;

import com.agregator.Agregator.DTO.*;
import com.agregator.Agregator.Entity.Address;
import com.agregator.Agregator.Entity.AggregatorSpecialist;
import com.agregator.Agregator.Entity.Customer;
import com.agregator.Agregator.Entity.Organization;
import com.agregator.Agregator.Services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRATION')")
public class AdminController {
    @Autowired
    private CustumerService customerService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/search")
    public ResponseEntity<?> searchCustomerByEmail() {
        try {
            // Получаем email из JWT
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Email: "+ email);
            // Ищем пользователя по email
            AggregatorSpecialist AggregatorSpecialist = adminService.findCustomerByEmail(email);
            if (AggregatorSpecialist != null) {
                return ResponseEntity.ok(AggregatorSpecialist);
            } else {
                return ResponseEntity.status(404).body("Admin not found");
            }
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователя", e);
            return ResponseEntity.status(500).body("Ошибка при поиске покупателя");
        }
    }


    @GetMapping("/all/Customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/Create/Customers")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerRegistrationDTO customer) {
        return registrationService.registerCustomer(customer);
    }

    @PutMapping("Update")
    public ResponseEntity<?> updateCustomer(@RequestBody CustumerDTO custumerDTO, @RequestParam Integer id, @RequestParam String email) {
        try {
            CustumerDTO updatedCustomer = customerService.updateCustumer(custumerDTO, id, email);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("Customer/{email}")
    public void deleteCustomer(@PathVariable String email) {
        customerService.deleteCustomerByEmail(email);
    }
    @GetMapping("/organizations")
    public List<OrganizationDTO> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("Organization/{id}")
    public Organization getOrganization(@PathVariable Integer id) {
        return organizationService.getOrganizationById(id);
    }

    @PostMapping("Organization/create")
    public ResponseEntity<?>  createOrganization(@RequestBody CreateOrganizationDTO organization) {
        return registrationService.registerOrganization(organization);
    }

    @PutMapping("Organization/update/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable int id, @RequestBody OrganizationDTO org) {
        return organizationService.updateOrganization(id, org);
    }

    @DeleteMapping("Organization/delete/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable int id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok("Организация удалена");
    }

    // Адреса конкретной организации
    @GetMapping("Organization/{id}/addresses")
    public List<Address> getOrganizationAddresses(@PathVariable int id) {
        return organizationService.getAddressesByOrganization(id);
    }

    @PostMapping("Organization/{id}/addresses")
    public Address addAddress(@PathVariable int id, @RequestBody Address address) {
        return organizationService.addAddressToOrganization(id, address);
    }

    @DeleteMapping("Organization/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId) {
        organizationService.deleteAddress(addressId);
        return ResponseEntity.ok("Адрес удалён");
    }

    // Создание ServiceType
    @PostMapping("Service/serviceTypes")
    public ResponseEntity<ServiceTypeDTO> createServiceType(@RequestBody ServiceTypeDTO dto) {
        return ResponseEntity.ok(serviceService.createServiceType(dto));
    }

    // Обновление ServiceType
    @PutMapping("Service/serviceTypes/{id}")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@PathVariable Integer id, @RequestBody ServiceTypeDTO dto) {
        return ResponseEntity.ok(serviceService.updateServiceType(id, dto));
    }

    // Удаление ServiceType
    @DeleteMapping("Service/serviceTypes/{id}")
    public ResponseEntity<?> deleteServiceType(@PathVariable Integer id) {
        serviceService.deleteServiceType(id);
        return ResponseEntity.noContent().build();
    }

    // Создание ServiceDetail
    @PostMapping("Service/serviceDetails")
    public ResponseEntity<ServiceDetailWithtTypeDTO> createServiceDetail(@RequestBody ServiceDetailWithtTypeDTO dto) {
        return ResponseEntity.ok(serviceService.createServiceDetail(dto));
    }

    // Обновление ServiceDetail
    @PutMapping("Service/serviceDetails/{id}")
    public ResponseEntity<ServiceDetailWithtTypeDTO> updateServiceDetail(@PathVariable Integer id, @RequestBody ServiceDetailWithtTypeDTO dto) {
        return ResponseEntity.ok(serviceService.updateServiceDetail(id, dto));
    }

    // Удаление ServiceDetail
    @DeleteMapping("Service/serviceDetails/{id}")
    public ResponseEntity<Void> deleteServiceDetail(@PathVariable Integer id) {
        serviceService.deleteServiceDetail(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("Connection/all")
    public  ResponseEntity<List<ConnectionRequestAdminDTO>> watchAllConnection(){
        List<ConnectionRequestAdminDTO> list = adminService.getAllConnectionRequests();
        return ResponseEntity.ok(list);
    }

    @GetMapping("Connection/filter")
    public ResponseEntity<List<ConnectionRequestAdminDTO>> getConnectionsByStatus(@RequestParam String status) {
        List<ConnectionRequestAdminDTO> list = adminService.getConnectionRequestsByStatus(status);
        return ResponseEntity.ok(list);
    }

    @PostMapping("Connection/UpdateStatus")
    public ResponseEntity<String> moveToInProgress(@RequestParam int connectionRequestId,@RequestParam String status) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return adminService.UpdateStatus(connectionRequestId, email, status);
    }
    @GetMapping("Connection/status")
    public ResponseEntity<ConnectionOrganizationDTO> getConnectionStatusByOrganization(@RequestParam int id){
        return adminService.getConnectionStatusByOrganization(id);
    }
}
