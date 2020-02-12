<#macro page>
    <!doctype html>
    <html lang="en">
        <head>
            <title>Simple Server practice</title>
            <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
        </head>
        <body>
            <#include "navbar.ftl">                         <!-- navigation bar -->
            <div class="container-fluid">

                <tbody>
                    <tr>
                        <td class="align-top">
                            <#nested>                       <!-- nested page -->
                        </td>
                        <td class="align-bottom">
                            <#include "footer.ftl">         <!-- footer -->
                        </td>
                    </tr>
                </tbody>
            </div>

            <!-- jquery & BOotstrap JS -->
            <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js">
            </script>
            <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js>
            </script>
        </body>
    </html>
</#macro>