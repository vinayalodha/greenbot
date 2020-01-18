$(document).ready(function () {
    var jsonObject=JSON.parse($('code').html());
    var parsedJsonString = JSON.stringify(jsonObject,null, 4);
    $('code').html(parsedJsonString);
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