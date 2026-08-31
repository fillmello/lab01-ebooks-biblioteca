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
- **Criação de login na tela de entrada.** Não é um caso de uso do enunciado, que trata o cadastro
  de usuários como responsabilidade da equipe da biblioteca. Foi acrescentado para o protótipo poder
  ser demonstrado com um aluno novo, sem depender dos dados de exemplo. Por isso a tela só cria
  aluno: liberar a criação de bibliotecário deixaria qualquer pessoa assumir o perfil que cadastra
  eBooks e renova o catálogo.
- **Cadastro de período de acesso no menu do bibliotecário.** Não é um caso de uso do enunciado, mas
  sem ele o catálogo gerado pela renovação ficaria sem período e nenhum aluno conseguiria montar a
  estante no semestre seguinte.
- **Uma única classe de persistência.** O roteiro mostra um `EBookRepositorioArquivo`, mas o
  protótipo precisa gravar cinco arquivos. Chegamos a criar uma classe por arquivo e desfizemos:
  as cinco eram o mesmo código de leitura e escrita, mudando só os campos de cada linha. Ficou um
  `BibliotecaRepositorioArquivo` com dois métodos de apoio, `escrever` e `ler`, reaproveitados por
  todos os arquivos.
- **Retorno `boolean` nas regras de estante e exceção no cadastro.** Limite atingido e período
  fechado são respostas esperadas do fluxo e voltam como `false`; dado obrigatório faltando é erro
  de preenchimento e vira `IllegalArgumentException`, capturada pelo menu.

## Regra do enunciado que ficou de fora

O enunciado não diz o que acontece com a estante do aluno quando um título perde a licença. Optamos
por retirar da estante os títulos não renovados, para não deixar na estante um eBook que saiu do
catálogo. Os itens que renovaram permanecem.
