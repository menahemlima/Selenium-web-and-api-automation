# Automação com Selenium + RestAssured

## 1. Web

#### Análise dos testes:

O campo From Employeer não permite valores com string, apenas valores números.
Foi testado 2 cenários para validar esse campo. Usando números e textos.




## 2. API

#### Análise dos testes:

```Os testes realizados na aplicação API apresentaram comportamentos diferentes do resultado esperado, caracterizando possíveis falhas:```

1. Uma simulação para um mesmo CPF retorna um HTTP Status 409 com a mensagem 
"CPF já existente":

   * Status Code FAIL - Retornando status code(400) deveria aparecer (409).
   
   * Message FAIL - Retornando mensagem "CPF duplicado", deveria aparecer "CPF já existente".
   
   
2.  Retorna HTTP Status 204 se não existir simulações cadastradas:

    *   Status Code FAIL - Retornando status code(204) deveria aparecer (200).

3. Retorna o HTTP Status 204 se simulação for removida com sucesso:

   * Status Code FAIL - Retornando status code(200) deveria aparecer (204).

4. Retorna o HTTP Status 404 com a mensagem "Simulação não encontrada" se não existir a simulação pelo ID informado:

   * Status Code FAIL - Retornando status code(200) deveria aparecer (404).

