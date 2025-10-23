package upce.nnpda.semA.service;

import org.springframework.stereotype.Service;
import upce.nnpda.semA.repository.AttachmentRepository;

@Service
public class AttachmentService {

    AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }
}
