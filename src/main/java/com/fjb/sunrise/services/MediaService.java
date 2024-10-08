package com.fjb.sunrise.services;

import com.fjb.sunrise.exceptions.BadRequestException;
import com.fjb.sunrise.exceptions.NotFoundException;
import com.fjb.sunrise.models.Media;
import com.fjb.sunrise.models.User;
import com.fjb.sunrise.repositories.MediaRepository;
import com.fjb.sunrise.repositories.UserRepository;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService {

    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    @Transactional
    public Media store(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            throw new BadRequestException("File is null");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileCode = UUID.randomUUID().toString();
        Media media;
        try {
            media = Media.builder()
                .name(fileName)
                .type(file.getContentType())
                .data(file.getBytes())
                .fileCode(fileCode)
                .build();
        } catch (IOException e) {
            throw new BadRequestException("Error when save file");
        }

        return mediaRepository.save(media);
    }

    @Transactional
    public Media getMedia(String fileCode) {
        return mediaRepository.findByFileCode(fileCode);
    }

    @Transactional
    public Stream<Media> getAllMedias() {
        return mediaRepository.findAll().stream();
    }

    @Transactional
    public Media getMediaOfUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmailOrPhone(name);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (user.getFileCode() == null) {
            return new Media();
        }
        return getMedia(user.getFileCode());
    }
}
