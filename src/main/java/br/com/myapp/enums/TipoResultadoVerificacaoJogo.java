package  br.com.myapp.enums;

public enum TipoResultadoVerificacaoJogo {

	NADA("N�O GANHOU NADA!"), QUADRA("Fez a QUADRA. Parab�ns!"), QUINA("Fez a QUINA. Parab�ns!"), SENA("Fez a SENA. Parab�ns!");

	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private TipoResultadoVerificacaoJogo(String desc) {
		this.desc = desc;
	}
}