package dk.kea.tech.project.simplenotes.Notes;

import dk.kea.tech.project.simplenotes.User.User;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Service
public class HibernateSearchService {


    @Autowired
    private final EntityManager centityManager;


    @Autowired
    public HibernateSearchService(EntityManager entityManager) {
        super();
        this.centityManager = entityManager;
    }


    public void initializeHibernateSearch() {

        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public List<Note> fuzzySearch(String searchTerm, User user) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Note.class).get();
        Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1).onFields("title", "content")
                .matching(searchTerm).createQuery();
        Query combinedQuery = qb
                .bool()
                .must(qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1).onFields("title", "content")
                        .matching(searchTerm).createQuery())
                .must(qb.keyword()
                        .onField("user.username").matching(user.getUsername())
                        .createQuery())
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(combinedQuery, Note.class);

        // execute search

        List<Note> NotesList = null;
        try {
            NotesList = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            nre.printStackTrace();

        }

        return NotesList;


    }
}