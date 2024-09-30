# language: pt

Funcionalidade: Mensagem

  @smoke @high
  Cenario: Registrar mensagem
    Quando registrar uma nova mensagem
    Então a mensagem é registrada com sucesso
    E deve ser apresentada

  @smoke @high
  Cenario: Buscar mensagem
    Dado que uma mensagem já foi publicada
    Quando efetuar a busca da mensagem
    Então a mensagem é exibida com sucesso

  @low
  Cenario: Alterar mensagem
    Dado que uma mensagem já foi publicada
    Quando efetuar a requisição para alterar a mensagem
    Então a mensagem é atualizada com sucesso
    E deve ser apresentada

  @high
  Cenario: Remover mensagem
    Dado que uma mensagem já foi publicada
    Quando requisitar a remoção da mensagem
    Então a mensagem é removida com sucesso

  @ignore
  Cenario: Remover mensagem
    Dado que uma mensagem já foi publicada
    Quando requisitar a remoção da mensagem
    Então a mensagem é removida com sucesso
