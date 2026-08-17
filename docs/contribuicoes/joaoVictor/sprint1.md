# Contribuições, Sprint 1 (Lab01S01), João Victor

## Semana 1 (10/08/2026)

**Contribuição:** participei da análise da descrição do sistema no enunciado do Laboratório 01,
junto com o Filipe. Levantamos os atores (Aluno e Bibliotecário) e listamos, em brainstorm, as
ações que cada um realiza no sistema, o que originou os 8 casos de uso do projeto. Na divisão de
tarefas do grupo, fiquei responsável pelas histórias de usuário.

**Decisões:**

- Dividimos o trabalho por tipo de artefato, e não por caso de uso: o Filipe assumiu o diagrama e a
  modelagem em PlantUML, e eu assumi as histórias de usuário. Assim cada um trabalha em um arquivo
  diferente e evitamos conflito de merge nos mesmos trechos.
- Acordamos que cada história teria o mesmo número do caso de uso correspondente, para manter a
  rastreabilidade entre o diagrama e este documento.

## Semana 2 (17/08/2026)

**Contribuição:** escrevi as 8 histórias de usuário em `docs/historias-de-usuario.md` (HU01 a
HU08), uma para cada caso de uso do diagrama, extraindo as regras diretamente da descrição do
sistema no enunciado. Também montei a tabela de referência no topo do arquivo, relacionando cada
história ao caso de uso e ao ator correspondente.

**Decisões:**

- Segui o formato "Como \<persona>, \<função>, eu quero \<ação>, para que \<benefício>", sempre com
  o benefício explícito, para não cair no erro de história vaga apontado no roteiro (por exemplo,
  "Como aluno, eu quero usar o sistema").
- Usei personas nomeadas (Ana, Bruno e Carla) em vez de apenas "aluno" e "bibliotecário", para
  deixar a leitura mais concreta, mantendo cada persona sempre no mesmo papel para não confundir
  quem é o ator de cada história.
- Trouxe para dentro das histórias os números que são regra de negócio do enunciado: o limite de 4
  eBooks obrigatórios e 2 livres na HU02, os 60 acessos simultâneos na HU04 e o mínimo de 3 alunos
  na HU08. Sem isso as histórias ficariam genéricas e não seriam testáveis, que é o "T" do INVEST.
- Escrevi a HU01 com uma persona genérica ("usuária do sistema") em vez de aluno ou bibliotecário,
  porque o enunciado diz que **todos** os usuários têm senha para validação do login. A história
  vale para os dois perfis.
- Mantive "Registrar estatística de uso" (HU07) sob a ótica da bibliotecária, e não do aluno: quem
  se beneficia do acompanhamento dos títulos mais usados é a biblioteca, mesmo que o gatilho seja a
  ação do aluno de adicionar um eBook à estante.
- Escrevi uma história por caso de uso, em vez de agrupar adicionar e remover em uma só, para
  manter as histórias pequenas e independentes, conforme os critérios INVEST.

**Uso de IA:** usei o Claude (Anthropic) para revisar a redação e a consistência das histórias
entre si, verificando se todas seguiam o mesmo formato e se nenhuma regra do enunciado tinha ficado
de fora. O conteúdo e as decisões acima foram definidos e revisados por mim antes do commit.
