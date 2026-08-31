# Comparação entre os modelos e o protótipo

Documento pedido no Passo 14 do roteiro da Sprint 3: o que foi modelado nas Sprints 1 e 2, o que foi
de fato implementado e o motivo de cada mudança.

## O que se manteve

Os 8 casos de uso da Sprint 1 foram implementados, sem nenhum removido ou acrescentado, e todas as
classes do diagrama da Sprint 2 continuam existindo com o mesmo papel. As agregações e composições
não mudaram: `Aluno` continua composto por `Estante`, `ItemEstante` continua agregando `EBook` e
`Catalogo` continua agregando `EBook` e compondo `PeriodoAcesso`.

## Mudanças no modelo, com o motivo

| Mudança | Motivo |
| --- | --- |
| `Usuario.autenticar` voltou ao modelo | O método tinha sido retirado na Sprint 2 por ser só um stub. O Passo 8 da Sprint 3 pede o login implementado, e a validação da senha é a realização direta do UC01. |
| `Estante.adicionar` e `Estante.remover` passaram a receber `PeriodoAcesso` e a data | O enunciado só permite alterar a estante durante o período de acesso. Sem a data e o período, a estante não teria como aplicar essa regra. |
| `Aluno.adicionarEBook` passou a receber `SistemaEstatisticas` | Garante o `<<include>>` de UC02 para UC07 dentro do modelo: a estatística é registrada logo depois de a adição dar certo, sem depender de a interface lembrar de chamá-la. |
| `Aluno.encerrarAcessoEBook` foi acrescentado | O UC04 exige que uma licença liberada desbloqueie um novo acesso. Sem um método para encerrar o acesso, `Licenca.liberarAcesso` nunca seria chamado. |
| `EBook` ganhou `equals` e `hashCode` por título | O título passou a identificar o eBook nos arquivos de dados, então as comparações em memória precisam seguir o mesmo critério. |
| `Catalogo` ganhou `buscarPorTitulo`, `adicionarPeriodo`, `listarPeriodos`, `periodoAbertoEm`, `podeAdicionar`, `periodoDeAcessoEncerrado` e `contarAlunosCom` | Métodos de consulta necessários para a interface: localizar o eBook digitado, saber se a estante pode ser alterada hoje e mostrar quantos alunos têm cada título antes de renovar. |
| `Estante` ganhou `restaurar`, `vagasRestantes` e `descartarNaoLicenciados` | `restaurar` recarrega os itens gravados sem repetir a validação do período; `vagasRestantes` mostra ao aluno quantas vagas sobram; `descartarNaoLicenciados` retira da estante os títulos que perderam a licença na renovação. |
| Pacotes `persistencia` e a classe `MenuPrincipal` entraram no diagrama | São a entrega da Sprint 3, interface e persistência, e não existiam no projeto estrutural da Sprint 2. |

## Decisões de implementação que o modelo não mostra

- **Limite da licença.** O enunciado fixa 60 acessos simultâneos como máximo. `Licenca` aceita um
  limite menor, validado entre 1 e 60, porque a licença "define quantos alunos podem acessá-lo ao
  mesmo tempo" e o 60 é o teto. O eBook de exemplo "Revista Brasileira de Computacao" tem limite 1,
  o que permite demonstrar o bloqueio sem abrir 60 acessos.
- **Acessos simultâneos não são persistidos.** Um acesso vale enquanto está aberto, então
  `acessosAtivos` é estado de execução: começa em zero a cada vez que o programa sobe, e os acessos
  abertos pelo aluno são liberados quando ele sai do menu.
- **Cadastro de usuários, nos dois menus.** O enunciado diz que a equipe da biblioteca mantém as
  informações sobre os bibliotecários e os alunos, mas isso não virou caso de uso na Sprint 1. Na
  implementação ficou no menu do bibliotecário, que cria aluno ou bibliotecário e é o único caminho
  para criar um bibliotecário novo. O menu inicial também cria conta, porém só de aluno, para o
  protótipo poder ser demonstrado com um aluno novo sem depender dos dados de exemplo: liberar ali a
  criação de bibliotecário deixaria qualquer pessoa assumir o perfil que cadastra eBooks e renova o
  catálogo.
- **Cadastro de período de acesso no menu do bibliotecário.** Não é um caso de uso do enunciado, mas
  sem ele o catálogo gerado pela renovação ficaria sem período e nenhum aluno conseguiria montar a
  estante no semestre seguinte.
- **Persistência por serialização, em um arquivo só.** Começamos com texto separado por ponto e
  vírgula, em cinco arquivos e cinco classes de repositório. Trocamos por serialização de objetos,
  uma das estratégias que o roteiro apresenta, porque o código de gravar e ler ficou muito menor: o
  Java grava o objeto inteiro, então sumiram a montagem das linhas, o `split`, as conversões de
  texto para enum, data e número, e a religação entre a estante do aluno e o eBook do catálogo.
  Ficou uma classe, `BibliotecaRepositorioArquivo`, com `salvar`, `carregar` e os dados de exemplo
  da primeira execução. Os três objetos vão no mesmo arquivo, e não em três, para o eBook da estante
  continuar sendo o mesmo objeto do catálogo e a contagem de acessos simultâneos bater nos dois
  lugares. Em troca, o arquivo é binário e não dá para conferir em um editor de texto, que era a
  vantagem do formato anterior.
- **Retorno `boolean` nas regras de estante e exceção no cadastro.** Limite atingido e período
  fechado são respostas esperadas do fluxo e voltam como `false`; dado obrigatório faltando é erro
  de preenchimento e vira `IllegalArgumentException`, capturada pelo menu.

## Regra do enunciado que ficou de fora

O enunciado não diz o que acontece com a estante do aluno quando um título perde a licença. Optamos
por retirar da estante os títulos não renovados, para não deixar na estante um eBook que saiu do
catálogo. Os itens que renovaram permanecem.
