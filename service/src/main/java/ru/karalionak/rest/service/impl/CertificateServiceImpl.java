package ru.karalionak.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.karalionak.rest.dao.CertificateDao;
import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.entity.Tag;
import ru.karalionak.rest.query.CertificateColumns;
import ru.karalionak.rest.query.Direction;
import ru.karalionak.rest.query.OrderedViewQuery;
import ru.karalionak.rest.query.SortingContext;
import ru.karalionak.rest.service.CertificateService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateDao certificateDao;
    private final TagServiceImpl tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagServiceImpl tagService) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
    }

    @Override
    @Transactional
    public void create(Certificate certificate) {
        long generatedId = certificateDao.create(certificate);
        Set<Tag> tags = certificate.getTags();
        addTagsToCertificate(generatedId, tags);
    }

    @Override
    @Transactional
    public void update(Certificate certificate) {
        certificateDao.update(certificate);
        Set<Tag> tags = certificate.getTags();
        if (tags != null) {
            addTagsToCertificate(certificate.getId(), tags);
        }
    }

    @Override
    public void delete(long id) {
        certificateDao.delete(id);
    }

    @Override
    public Optional<Certificate> findById(long id) {
        return certificateDao.findById(id);
    }

    @Override
    @Transactional
    public List<Certificate> getAllWithTags(String tagName, String regex,
                                            Map<CertificateColumns, Direction> context) {
        SortingContext sortingContext = new SortingContext(context);
        OrderedViewQuery orderedViewQuery = OrderedViewQuery.builder()
                .filteringByTagName(tagName)
                .filteringByNameOrDescription(regex)
                .sortingQuery(sortingContext)
                .build();
        return certificateDao.findCertificateWithTags(orderedViewQuery);
    }

    private void addTagsToCertificate(long id, Set<Tag> tags) {
        List<Tag> existsTags = tagService.findExistingTags(tags);
        List<String> names = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        List<String> existsNames = existsTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        names.removeAll(existsNames);
        List<Long> generatedIds = tagService.createTags(names);
        certificateDao.addTags(id, generatedIds);
    }

}
