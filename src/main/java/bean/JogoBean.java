package bean;

import dao.CampeonatoDAO;
import dao.JogoDAO;
import dao.TimeDAO;
import entidades.Campeonato;
import entidades.Jogo;
import entidades.Time;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import java.util.Map;

@ManagedBean
@ViewScoped
public class JogoBean {

    private Jogo jogo = new Jogo();
    private Integer campeonatoId; 
    private List<Campeonato> listaCampeonatos;
    private List<Jogo> listaJogos;
    private List<Time> listaTimes;
    private Time time;
    public Time getTime() {
        return time;
    }
    
    public List<Time> getListaTimes() {
        if (listaTimes == null) {
        	TimeDAO timeDAO = new TimeDAO();
            listaTimes = timeDAO.listar();
        }
        return listaTimes;
    }


    @PostConstruct
    public void init() {
        CampeonatoDAO campeonatoDAO = new CampeonatoDAO();
        listaCampeonatos = campeonatoDAO.listar();
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Integer getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(Integer campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public List<Campeonato> getListaCampeonatos() {
        return listaCampeonatos;
    }

    public List<Jogo> getListaJogos() {
        if (listaJogos == null) {
            JogoDAO jogoDAO = new JogoDAO();
            listaJogos = jogoDAO.listar();
        }
        return listaJogos;
    }

    public void salvar() {
        // Validação para evitar que time1 e time2 sejam iguais
        if (jogo.getTime1().equals(jogo.getTime2())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Time1 e Time2 não podem ser iguais."));
            return;
        }

        // Define a data de cadastro com a data e hora atual do sistema
        jogo.setDataCadastro(new Date());

        // Busca o campeonato pelo ID selecionado no formulário
        CampeonatoDAO campeonatoDAO = new CampeonatoDAO();
        Campeonato campeonato = campeonatoDAO.buscarPorId(campeonatoId);
        if (campeonato == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Campeonato não encontrado."));
            return;
        }
        jogo.setCampeonato(campeonato);

        // Salva o objeto Jogo no banco de dados
        JogoDAO jogoDAO = new JogoDAO();
        jogoDAO.salvar(jogo);

        // Adiciona uma mensagem de sucesso
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Jogo salvo com sucesso."));

        // Reseta os campos do formulário
        jogo = new Jogo();
        campeonatoId = null;
        listaCampeonatos = null; // Reseta a lista de campeonatos para recarregar
    }
    public void calcularResumo() {
    	TimeDAO timeDAO = new TimeDAO();
        Map<String, Time> timesMap = new HashMap<>();
        
        for (Jogo jogo : getListaJogos()) {
            processarJogo(jogo, timesMap, jogo.getTime1(), jogo.getGolsTime1(), jogo.getGolsTime2());
            processarJogo(jogo, timesMap, jogo.getTime2(), jogo.getGolsTime2(), jogo.getGolsTime1());
        }

        timeDAO.excluirTodos();
        for (Time time : timesMap.values()) {
            timeDAO.salvar(time);
        }
        
        listaTimes = timeDAO.listar();
    }

    private void processarJogo(Jogo jogo, Map<String, Time> timesMap, String nomeTime, int golsMarcados, int golsSofridos) {
        Time time = timesMap.getOrDefault(nomeTime, new Time());
        time.setNome(nomeTime);

        if (time.getVitorias() == null) time.setVitorias(0);
        if (time.getPontuacao() == null) time.setPontuacao(0);
        if (time.getDerrotas() == null) time.setDerrotas(0);
        if (time.getEmpates() == null) time.setEmpates(0);
        if (time.getGolsMarcados() == null) time.setGolsMarcados(0);
        if (time.getGolsSofridos() == null) time.setGolsSofridos(0);
        if (time.getSaldoDeGols() == null) time.setSaldoDeGols(0);

        if (golsMarcados > golsSofridos) {
            time.setVitorias(time.getVitorias() + 1);
            time.setPontuacao(time.getPontuacao() + 3);
        } else if (golsMarcados < golsSofridos) {
            time.setDerrotas(time.getDerrotas() + 1);
        } else {
            time.setEmpates(time.getEmpates() + 1);
            time.setPontuacao(time.getPontuacao() + 1);
        }

        time.setGolsMarcados(time.getGolsMarcados() + golsMarcados);
        time.setGolsSofridos(time.getGolsSofridos() + golsSofridos);
        time.setSaldoDeGols(time.getGolsMarcados() - time.getGolsSofridos());

        timesMap.put(nomeTime, time);
    }
    
    public void excluir(Jogo jogo) {
        if (jogo == null || jogo.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Jogo não encontrado."));
            return;
        }

        JogoDAO jogoDAO = new JogoDAO();
        try {
            jogoDAO.excluir(jogo.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Jogo excluído com sucesso."));
            listaJogos = null; // Reseta a lista de jogos para recarregar
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir o jogo."));
            e.printStackTrace();
        }

        // Reseta os campos do formulário
        this.jogo = new Jogo();
    }

}
