package org.example.model;

import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;

import java.io.Serializable;
import java.util.UUID;


public abstract class AbstractEntity implements Serializable,Entity{
    @PartitionKey
    private String discriminator;
    //if we need to combine more then one column value to form a single partition key, we use a composite partition key
    //the goal of the key is for the data placement, in addition to uniquely identifying the data.
    //As a result, the storage and retrieval of data become efficient

    @ClusteringColumn
    private UUID id; //Clustering Key -> clustering is a storage engine process of sorting the data within a partition and is based on the columns defined as the clustering key
    //By default Cassandra storage engine sorts the data in ascending order of clustering key columns

    public AbstractEntity(){}
    public AbstractEntity(UUID id,String discriminator) {
        this.id = id;
        this.discriminator =this.getClass().getSimpleName();
    }

    @Override
    public String getDiscriminator() {
        return discriminator;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
