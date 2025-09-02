package com.hotel.erp.repository;

import com.hotel.erp.HotelManagementErpApplication;
import com.hotel.erp.entity.Checkin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = HotelManagementErpApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckinRepositoryTest {

    @Autowired
    private CheckinRepository checkinRepository;

    @Test
    public void shouldFindByFolioNo() {
        // Given
        String folioNo = "F12345";
        Checkin checkin = new Checkin();
        checkin.setFolioNo(folioNo);
        checkin.setGuestName("John Doe");
        checkin.setContactNo("9876543210");
        checkin.setCheckInDate(LocalDate.now());
        checkin.setCheckOutDate(LocalDate.now().plusDays(2));
        checkin.setStatus("CHECKED_IN");
        checkinRepository.save(checkin);

        // When
        Optional<Checkin> found = checkinRepository.findByFolioNo(folioNo);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getGuestName()).isEqualTo("John Doe");
    }

    @Test
    public void shouldFindAllActiveCheckins() {
        // Given
        Checkin activeCheckin1 = new Checkin();
        activeCheckin1.setFolioNo("F12345");
        activeCheckin1.setGuestName("John Doe");
        activeCheckin1.setContactNo("9876543210");
        activeCheckin1.setCheckInDate(LocalDate.now());
        activeCheckin1.setCheckOutDate(LocalDate.now().plusDays(2));
        activeCheckin1.setStatus("CHECKED_IN");
        checkinRepository.save(activeCheckin1);

        Checkin activeCheckin2 = new Checkin();
        activeCheckin2.setFolioNo("F12346");
        activeCheckin2.setGuestName("Jane Smith");
        activeCheckin2.setContactNo("9876543211");
        activeCheckin2.setCheckInDate(LocalDate.now());
        activeCheckin2.setCheckOutDate(LocalDate.now().plusDays(3));
        activeCheckin2.setStatus("CHECKED_IN");
        checkinRepository.save(activeCheckin2);

        Checkin inactiveCheckin = new Checkin();
        inactiveCheckin.setFolioNo("F12347");
        inactiveCheckin.setGuestName("Bob Brown");
        inactiveCheckin.setContactNo("9876543212");
        inactiveCheckin.setCheckInDate(LocalDate.now().minusDays(3));
        inactiveCheckin.setCheckOutDate(LocalDate.now().minusDays(1));
        inactiveCheckin.setStatus("CHECKED_OUT");
        checkinRepository.save(inactiveCheckin);

        // When
        List<Checkin> activeCheckins = checkinRepository.findAllActiveCheckins();

        // Then
        assertThat(activeCheckins).hasSize(2);
        assertThat(activeCheckins.get(0).getStatus()).isEqualTo("CHECKED_IN");
        assertThat(activeCheckins.get(1).getStatus()).isEqualTo("CHECKED_IN");
    }

    @Test
    public void shouldFindByRoomNo() {
        // Given
        String roomNo = "101";
        Checkin checkin = new Checkin();
        checkin.setFolioNo("F12345");
        checkin.setGuestName("John Doe");
        checkin.setContactNo("9876543210");
        checkin.setRoomNo(roomNo);
        checkin.setCheckInDate(LocalDate.now());
        checkin.setCheckOutDate(LocalDate.now().plusDays(2));
        checkin.setStatus("CHECKED_IN");
        checkinRepository.save(checkin);

        // When
        List<Checkin> found = checkinRepository.findByRoomNo(roomNo);

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getRoomNo()).isEqualTo(roomNo);
    }

    @Test
    public void shouldFindCheckinsBetweenDates() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        Checkin checkin1 = new Checkin();
        checkin1.setFolioNo("F12345");
        checkin1.setGuestName("John Doe");
        checkin1.setContactNo("9876543210");
        checkin1.setCheckInDate(startDate);
        checkin1.setCheckOutDate(startDate.plusDays(2));
        checkin1.setStatus("CHECKED_IN");
        checkinRepository.save(checkin1);

        Checkin checkin2 = new Checkin();
        checkin2.setFolioNo("F12346");
        checkin2.setGuestName("Jane Smith");
        checkin2.setContactNo("9876543211");
        checkin2.setCheckInDate(startDate.plusDays(1));
        checkin2.setCheckOutDate(endDate);
        checkin2.setStatus("CHECKED_IN");
        checkinRepository.save(checkin2);

        Checkin checkin3 = new Checkin();
        checkin3.setFolioNo("F12347");
        checkin3.setGuestName("Bob Brown");
        checkin3.setContactNo("9876543212");
        checkin3.setCheckInDate(startDate.minusDays(2));
        checkin3.setCheckOutDate(startDate.minusDays(1));
        checkin3.setStatus("CHECKED_OUT");
        checkinRepository.save(checkin3);

        // When
        List<Checkin> found = checkinRepository.findCheckinsBetweenDates(startDate, endDate);

        // Then
        assertThat(found).hasSize(2);
        for (Checkin c : found) {
            assertThat(c.getCheckInDate().isEqual(startDate) || c.getCheckInDate().isAfter(startDate)).isTrue();
            assertThat(c.getCheckOutDate().isEqual(endDate) || c.getCheckOutDate().isBefore(endDate)).isTrue();
        }
    }

    @Test
    public void shouldCountActiveCheckins() {
        // Given
        Checkin activeCheckin1 = new Checkin();
        activeCheckin1.setFolioNo("F12345");
        activeCheckin1.setGuestName("John Doe");
        activeCheckin1.setContactNo("9876543210");
        activeCheckin1.setCheckInDate(LocalDate.now());
        activeCheckin1.setCheckOutDate(LocalDate.now().plusDays(2));
        activeCheckin1.setStatus("CHECKED_IN");
        checkinRepository.save(activeCheckin1);

        Checkin inactiveCheckin = new Checkin();
        inactiveCheckin.setFolioNo("F12347");
        inactiveCheckin.setGuestName("Bob Brown");
        inactiveCheckin.setContactNo("9876543212");
        inactiveCheckin.setCheckInDate(LocalDate.now().minusDays(3));
        inactiveCheckin.setCheckOutDate(LocalDate.now().minusDays(1));
        inactiveCheckin.setStatus("CHECKED_OUT");
        checkinRepository.save(inactiveCheckin);

        // When
        Long count = checkinRepository.countActiveCheckins();

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void shouldFindOverdueCheckins() {
        // Given
        LocalDate today = LocalDate.now();

        Checkin overdueCheckin = new Checkin();
        overdueCheckin.setFolioNo("F12345");
        overdueCheckin.setGuestName("John Doe");
        overdueCheckin.setContactNo("9876543210");
        overdueCheckin.setCheckInDate(today.minusDays(3));
        overdueCheckin.setCheckOutDate(today.minusDays(1));
        overdueCheckin.setStatus("CHECKED_IN");
        checkinRepository.save(overdueCheckin);

        Checkin activeCheckin = new Checkin();
        activeCheckin.setFolioNo("F12346");
        activeCheckin.setGuestName("Jane Smith");
        activeCheckin.setContactNo("9876543211");
        activeCheckin.setCheckInDate(today);
        activeCheckin.setCheckOutDate(today.plusDays(2));
        activeCheckin.setStatus("CHECKED_IN");
        checkinRepository.save(activeCheckin);

        // When
        List<Checkin> found = checkinRepository.findOverdueCheckins(today);

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCheckOutDate()).isBefore(today);
        assertThat(found.get(0).getStatus()).isEqualTo("CHECKED_IN");
    }
}
