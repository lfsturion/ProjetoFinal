# Disciplina Desenvolvimento para Dispositivos Móveis II 
# Curso Especialização em Desenvolvimento de Sistemas Web e Aplicativos Móveis
# Professor Edivaldo Serafim

O projeto utiliza o banco de dados MariaDB que está sendo executado através do XAMPP.

Antes de executar o projeto no Android Studio é necessário executar o script "transactions.sql" para criação do banco de dados, das tabelas e do usuário teste para login na aplicação.
Usuário: teste@teste.com
Senha: 123456

Na pasta "htdocs" do XAMPP crie uma pasta chamada "projetofinal" e coloco todos arquivos .php e o arquvo .env dentro dessa pasta.
O arquivo "create_user.php" foi utilizado para criação do usuário teste, não sendo necessário para o projeto no Android Studio.
A execução do "create_user.php" foi através de http://localhost/projetofinal/create_user.php, porém não é necessário executar novamente, o mesmo já será inserido através do script "transactions.sql".

Após o Apache e MariaDB iniciados no XAMPP, o banco de dados e as tabelas criadas, assim como inserido o usuário de teste, é possível iniciar o projeto no Android Studio.
