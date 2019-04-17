package io.freefair.screenshot_scheduler.repositories;

import io.freefair.screenshot_scheduler.models.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot, UUID> {
}
