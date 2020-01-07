$(document).ready(function () {
    debugger;
    $('code').html(JSON.stringify(JSON.parse($('code').html()), null, 4));
    $('#config-json').val(JSON.stringify(JSON.parse($('#config-json').val()), null, 4));
    
    $('pre code').each(function(i, block) {
        hljs.highlightBlock(block);
      });
    $('#analyze').click(function () {
        var request = {
            timeframe: $('#timeframe').val(),
            configjson: $('#config-json').val()
        };
        $.ajax({
            contentType: 'application/json',
            data: JSON.stringify(request),
            dataType: 'json',
            success: function (data) {
                console.log("device control succeeded");
            },
            error: function () {
                console.log("Device control failed");
            },
            processData: false,
            type: 'POST',
            url: '/'
        });

    });
});