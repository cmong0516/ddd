package com.example.ddd.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Table(name = "image")
public abstract class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    private Date uploadTime;

    protected Image() {

    }

    public Image(String path) {
        this.path = path;
        this.uploadTime = new Date();
    }

    protected String getPath() {
        return path;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public abstract String getURL();

    public abstract boolean hasThumbnail();

    public abstract String getThumbnailURL();

}
