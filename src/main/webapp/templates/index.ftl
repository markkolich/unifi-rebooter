<!doctype html>
<html lang="en">
<head>
  <title>Unifi Rebooter</title>
  <style type="text/css">
      table { border-collapse: collapse }
      td,th { border: 2px solid #555 }
  </style>
</head>
<body>

  <h1>Unifi Rebooter</h1>

  <table>
    <thead>
      <tr>
        <th>Hostname</th>
        <th>Username</th>
        <th>Cron</th>
      </tr>
    <thead>
    <tbody>
      <#list devices as device>
        <tr>
          <td><pre>${device.getHostname()}</pre></td>
          <td><pre>${device.getUsername()}</pre></td>
          <td><pre>${device.getCronExpression()}</pre></td>
        </tr>
      </#list>
    </tbody>
  </table>

  <h4>${buildVersion.getBuildNumber()} &ndash; ${buildVersion.getTimestamp()}</h4>

</body>
</html>