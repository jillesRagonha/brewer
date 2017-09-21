package com.algaworks.brewer.service.event.cerveja;

import com.algaworks.brewer.models.Cerveja;
import org.springframework.util.StringUtils;

public class CervejaSalvaEvento {
    private Cerveja cerveja;

    public CervejaSalvaEvento(Cerveja cerveja) {
        this.cerveja = cerveja;
    }

    public Cerveja getCerveja() {
        return cerveja;
    }

    public void setCerveja(Cerveja cerveja) {
        this.cerveja = cerveja;
    }

    public boolean temFoto(){
        return !StringUtils.isEmpty(cerveja.getFoto());
    }
}
