package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore_with_me.model.Application;
import ru.practicum.explore_with_me.repositiry.ApplicationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    /**
     * Выдать информацию о приложении по его названию из БД.
     * @param appMame название приложения.
     * @return приложение.
     */
    @Override
    public Optional<Application> getByName(String appMame) {
        return applicationRepository.findByApp(appMame);
    }

    /**
     * Сохранить новую запись о новом приложении в БД.
     * @param application приложение.
     */
    @Override
    public Application save(Application application) {
        Application app = applicationRepository.save(application);
        log.info("Выполнено сохранение записи о новом приложении {}.", application);
        return app;
    }
}
