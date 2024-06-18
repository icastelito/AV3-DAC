package bean;

import dao.CampeonatoDAO;
import entidades.Campeonato;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class CampeonatoBean {

    private Campeonato campeonato = new Campeonato();

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public void salvar() {
        CampeonatoDAO campeonatoDAO = new CampeonatoDAO();
        try {
            campeonatoDAO.salvar(campeonato);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Campeonato salvo com sucesso."));
            campeonato = new Campeonato(); // Resetar o formulário
            System.out.println("pau");
        } catch (Exception e) {
        	System.out.println("BUCETA");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar o campeonato."));
            e.printStackTrace();
        }
    }
}
