package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Main {
	private static final String FILENAME = "input.txt";
	private static final String EMAIL_FROM = "joaogouveiaparatestes@gmail.com";
	private static final String EMAIL_PASS = "developer10021989";
	
	
	private static ArrayList<Responsavel> responsaveis;
	private static ArrayList<Dependente> dependentes;
	private static ArrayList<Object> participantes;
	
	public static void main(String[] args) {
		responsaveis = new ArrayList<Responsavel>();
		dependentes = new ArrayList<Dependente>();
		participantes = new ArrayList<Object>();
		
		preencherArrays();
		
		participantes.addAll(responsaveis);
		participantes.addAll(dependentes);
		
		sorteia();
		
		for(Responsavel resp : responsaveis){
			System.out.println(resp.getNome() + " tirou " + resp.getSorteado());
		}
		for(Dependente dep : dependentes){
			System.out.println(dep.getNome() + " tirou " + dep.getSorteado());
		}
		
		//envio de emails
		SendMail sm = new SendMail("smtp.gmail.com","465");
		for(Responsavel r : responsaveis){
			sm.sendMail(EMAIL_FROM, r.getEmail(), "Amigo Secreto", "Ola "+ r.getNome() + " voce tirou " + r.getSorteado());
		}
		
		for(Dependente d : dependentes){
			sm.sendMail(EMAIL_FROM, d.getResponsavel().getEmail(), "Amigo Secreto", "Ola "+ d.getResponsavel().getNome() + " avise o(a) " + d.getNome() +" que ele(a) tirou " + d.getSorteado() + " no amigo secreto!");
		}
		System.out.println("Emails enviados com sucesso");
		
	}
	
	private static void sorteia(){
		Random rn = new Random();
		int idSorteado;
		boolean saiu;
		boolean seTirou;
		for(Responsavel responsavel : responsaveis){
			do{
				if(participantes.size() > 0){
					idSorteado = rn.nextInt(participantes.size());
				}else
					break;
				if(participantes.get(idSorteado) instanceof Responsavel){
					Responsavel resp = (Responsavel) participantes.get(idSorteado);
					saiu = jaSaiu(resp.getNome());
					seTirou = responsavel.getNome().equals(resp.getNome()) ? true : false;
					if(!saiu && !seTirou)
						responsavel.setSorteado(resp.getNome());					
				}else{
					Dependente dep = (Dependente) participantes.get(idSorteado);
					saiu = jaSaiu(dep.getNome());
					seTirou = responsavel.getNome().equals(dep.getNome()) ? true : false;
					if(!saiu && !seTirou)
						responsavel.setSorteado(dep.getNome());
				}
				if(!saiu && !seTirou)
					participantes.remove(idSorteado);
					
			}while(saiu || seTirou);			
		}
		
		for(Dependente dependente : dependentes){
			do{
				if(participantes.size() > 0){
					idSorteado = rn.nextInt(participantes.size());
				}else
					break;
				
				if(participantes.get(idSorteado) instanceof Responsavel){
					Responsavel resp = (Responsavel) participantes.get(idSorteado);
					saiu = jaSaiu(resp.getNome());
					seTirou = dependente.getNome().equals(resp.getNome()) ? true : false;
					if(!saiu && !seTirou)
						dependente.setSorteado(resp.getNome());
				}else{
					Dependente dep = (Dependente) participantes.get(idSorteado);
					saiu = jaSaiu(dep.getNome());
					seTirou = dependente.getNome().equals(dep.getNome()) ? true : false;
					if(!saiu && !seTirou)
						dependente.setSorteado(dep.getNome());
				}
				if(!saiu && !seTirou)
					participantes.remove(idSorteado);
			}while(saiu || seTirou);
		}
	}
	
	private static boolean jaSaiu(String nome){
		for(Responsavel responsavel : responsaveis){
			if(responsavel.getSorteado() != null && responsavel.getSorteado().equals(nome)){
				return true;
			}
		}
		
		for(Dependente dependente : dependentes){
			if(dependente.getSorteado() != null && dependente.getSorteado().equals(nome)){
				return true;
			}
		}
		return false;
	}

	private static void preencherArrays(){
		BufferedReader br = null;
		FileReader fr = null;
		Responsavel ultimoResponsavel = new Responsavel();

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				if(sCurrentLine.charAt(0) == 'R'){
					Responsavel r = new Responsavel();
					String splited[] = sCurrentLine.split(" ");
					r.setNome(splited[1]);
					r.setEmail(splited[2]);
					responsaveis.add(r);
					ultimoResponsavel = r;
				}else if(sCurrentLine.charAt(0) == 'D'){
					Dependente d = new Dependente();
					String splited[] = sCurrentLine.split(" ");
					d.setNome(splited[1]);
					d.setResponsavel(ultimoResponsavel);
					dependentes.add(d);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
}
