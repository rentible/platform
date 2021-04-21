package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.model.Site;
import hu.lanoga.flatshares.repository.SiteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService implements CrudOperations<Site> {

    private final SiteMapper siteMapper;

    public SiteService(SiteMapper siteMapper) {
        this.siteMapper = siteMapper;
    }

    @Override
    public int save(Site site) {
        return siteMapper.save(site);
    }

    @Override
    public int update(Site site) {
        return 0;
    }

    @Override
    public Site findOne(int id) {
        return siteMapper.findOne(id);
    }

    @Override
    public List<Site> findAll() {
        return siteMapper.findAll();
    }

    @Override
    public void delete(int id) {
        siteMapper.delete(id);
    }
}
