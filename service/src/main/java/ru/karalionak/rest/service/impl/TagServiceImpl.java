package ru.karalionak.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.karalionak.rest.dao.TagDao;
import ru.karalionak.rest.entity.Tag;
import ru.karalionak.rest.service.TagService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public void create(Tag tag) {
        tagDao.create(tag);
    }

    @Override
    public void delete(long id) {
        tagDao.delete(id);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return tagDao.findById(id);
    }

    @Override
    public List<Tag> findExistingTags(Set<Tag> tags) {
        return tagDao.findExistingTags(tags);
    }

    @Override
    public List<Long> createTags(List<String> names) {
        return tagDao.createTags(names);
    }

    @Override
    public List<Tag> findByCertificateId(long id) {
        return tagDao.findByCertificateId(id);
    }
}
