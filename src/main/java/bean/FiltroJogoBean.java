package bean;

import dao.JogoDAO;
import entidades.Jogo;
import entidades.Time;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class FiltroJogoBean implements Serializable {

    private Time time;
    private List<Jogo> listaJogos;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public List<Jogo> getListaJogos() {
        return listaJogos;
    }

    public void setListaJogos(List<Jogo> listaJogos) {
        this.listaJogos = listaJogos;
    }

    public void localizarJogosPorTime() {
        if (time == null || time.getId() == null) {
            // Se nenhum time foi selecionado
            listaJogos = null;
        } else {
            // Buscar os jogos do time selecionado
            JogoDAO jogoDAO = new JogoDAO();
            listaJogos = jogoDAO.listarJogosPorTime(time.getId());
        }
    }
}
