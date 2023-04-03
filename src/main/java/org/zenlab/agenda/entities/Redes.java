/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.zenlab.agenda.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author zenlaboratory
 */
@Entity
@Table(name = "redes", catalog = "agenda", schema = "")
@NamedQueries({
    @NamedQuery(name = "Redes.findAll", query = "SELECT r FROM Redes r"),
    @NamedQuery(name = "Redes.findById", query = "SELECT r FROM Redes r WHERE r.id = :id"),
    @NamedQuery(name = "Redes.findByEmail", query = "SELECT r FROM Redes r WHERE r.email = :email"),
    @NamedQuery(name = "Redes.findByTwitter", query = "SELECT r FROM Redes r WHERE r.twitter = :twitter"),
    @NamedQuery(name = "Redes.findByFacebook", query = "SELECT r FROM Redes r WHERE r.facebook = :facebook"),
    @NamedQuery(name = "Redes.findByInstagram", query = "SELECT r FROM Redes r WHERE r.instagram = :instagram"),
    @NamedQuery(name = "Redes.findByYoutube", query = "SELECT r FROM Redes r WHERE r.youtube = :youtube"),
    @NamedQuery(name = "Redes.findByTwich", query = "SELECT r FROM Redes r WHERE r.twich = :twich")})
public class Redes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "twitter", length = 100)
    private String twitter;
    @Column(name = "facebook", length = 100)
    private String facebook;
    @Column(name = "instagram", length = 100)
    private String instagram;
    @Column(name = "youtube", length = 100)
    private String youtube;
    @Column(name = "twich", length = 100)
    private String twich;
    @JoinColumn(name = "contacto_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contacto contactoId;

    public Redes() {
    }

    public Redes(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getTwich() {
        return twich;
    }

    public void setTwich(String twich) {
        this.twich = twich;
    }

    public Contacto getContactoId() {
        return contactoId;
    }

    public void setContactoId(Contacto contactoId) {
        this.contactoId = contactoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Redes)) {
            return false;
        }
        Redes other = (Redes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.zenlab.agenda.Redes[ id=" + id + " ]";
    }
    
}
