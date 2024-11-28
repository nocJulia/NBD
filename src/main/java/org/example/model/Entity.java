package org.example.model;

import java.util.UUID;

public interface Entity {
    public UUID getId();
    public void setId(UUID id);
    public String getDiscriminator();
    public void setDiscriminator(String discriminator);
}
