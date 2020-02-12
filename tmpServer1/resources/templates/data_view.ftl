<#import "common/bootstrap.ftl" as b>

<@b.page>
    <#if data?? && (data?size > 0)>
        <h4> ${displayName}'s repository </h4> <br>
        <table class="table table-striped">
            <thread>
                <tr>
                   <th>Title</th>
                   <th>Content</th>
                   <th></th>
                </tr>
            </thread>
            <tbody>
                <#list data as one_data>
                    <tr>
                        <td style="vertical-align:middle"><h3>${one_data.title}</h3></td>
                        <td style="vertical-align:middle"><h3>${one_data.content}</h3zx></td>
                        <td class="col-md-1" style="text-align:center;vertical-align:middle;">
                            <form method="post" action="/dataview">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="date" value="${date?c}">
                                <input type="hidden" name="code" value="${code}">
                                <input type="hidden" name="id" value="${one_data.id}">
                                <input type="image" src="/static/delete.png" width="24" height="24" boarder="0" alt="Delete" />
                            </form>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </#if>
    <form method="post" action="/dataview">
    <input type="hidden" name="action" value="add">
    <input type="hidden" name="date" value="${date?c}">
    <input type="hidden" name="code" value="${code}">
        <div class="row">
            <div class="col-md-8">
                <div class="form-group">
                    <label for="Title">Title</label>
                    <input type="text" class="form-control" name="title" placeholder="Title">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-8">
                <div class="form-group">
                    <label for="Content">Content</label>
                    <input type="text" class="form-control" name="content" placeholder="Content">
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-default">Submit</button>

    </form>

</@b.page>

<!--
    <div class="panel-body">
    <form method="post" action="/dataview">
    <input type="hidden" name="action" value="add">
    Title:<br>
    <input type="text" name="title" /><br>
    Content:<br>
    <input type="text" name="content" /><br>
    <input type="submit" value="Submit" />
    </form>
    </div>
-->