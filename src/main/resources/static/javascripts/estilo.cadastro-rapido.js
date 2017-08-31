$(function () {
    var modal = $('#modalCadastroRapidoEstilo');
    var botaoSalvar = modal.find('.js-modal-cadastro-estilo-salvar-btn');
    var form = modal.find('form');
    form.on('submit', function (e) {
        e.preventDefault()
    });
});