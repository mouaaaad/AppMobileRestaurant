package com.example.app_restaurant;
import org.junit.Test;
import com.example.app_restaurant.Model.Commentaire;
import com.example.app_restaurant.Model.menu;

import static org.junit.Assert.*;

public class CommentaireUnitTest {
    @Test
    public void Commentaire() {
        try {
            Commentaire com1=new Commentaire("titre1","commentaire1",5);
            Commentaire com2=new Commentaire("titre2","commentaire2","061212");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void CommetaireTestGeter()
    {
        Commentaire com1=new Commentaire("titre1","commentaire1",5);
        Commentaire com2=new Commentaire("titre2","commentaire2","061212");
        String titre1=com1.getTitre();
        String titre2=com2.getTitre();
        String commentaire1=com1.getCommentaire();
        String commentaire2=com2.getCommentaire();
        Float rate =com1.getRate();
        String user=com2.getUser();
        assertEquals(titre1, "titre1");
        assertEquals(titre2, "titre2");
        assertEquals(commentaire1, "commentaire1");
        assertEquals(commentaire2, "commentaire2");
        assertEquals(rate.toString(), "5.0");
        assertEquals(user, "061212");
    }
    @Test
    public void CommetaireTestSeter()
    {
        Commentaire com1=new Commentaire("titre1","commentaire1",5);
        Commentaire com2=new Commentaire("titre2","commentaire2","061212");
        com1.setTitre("titre0");
        com2.setTitre("titre3");
        com1.setCommentaire("commentaire0");
        com2.setCommentaire("commentaire3");
        com1.setRate(2);
        com2.setUser("061111");
        String titre1=com1.getTitre();
        String titre2=com2.getTitre();
        String commentaire1=com1.getCommentaire();
        String commentaire2=com2.getCommentaire();
        Float rate =com1.getRate();
        String user=com2.getUser();
        assertEquals(titre1, "titre0");
        assertEquals(titre2, "titre3");
        assertEquals(commentaire1, "commentaire0");
        assertEquals(commentaire2, "commentaire3");
        assertEquals(rate.toString(), "2.0");
        assertEquals(user, "061111");
    }


}
