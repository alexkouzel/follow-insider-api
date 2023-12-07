import { HttpUtils } from '/scripts/utils/http.js';
import { StringUtils } from '/scripts/utils/string.js';
import { Config } from '/scripts/root.js';

let LOG_COLORS = {
    'TRACE': '#8a50a9',
    'DEBUG': '#485aab',
    'INFO': '#69916a',
    'WARN': '#c5ae53',
    'ERROR': '#b44b4b',
}

export function initLogs(container) {
    let loader = Config.debugMode ? _loadMockLogs : _loadServerLogs;
    loader(logs => _insertLogs(logs, container));
}

function _loadServerLogs(onLoad) {
    let params = new URLSearchParams(location.search);

    let exclude = params.get('exclude') ?? '';
    let from = params.get('from') ?? 0;
    let to = params.get('to') ?? 100;

    let url = `/debug/logs?from=${from}&to=${to}&exclude=${exclude}`;

    HttpUtils.getJson(url).then(onLoad);
}

function _loadMockLogs(onLoad) {
    onLoad(_mockLogs());
}

function _insertLogs(logs, container) {
    logs.forEach(log => _insertLog(log, container));
}

function _insertLog(log, container) {
    let color = LOG_COLORS[log.type];

    let dateOptions = { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    let date = StringUtils.formatDate(log.date, dateOptions);

    let html = '';
    html += `<span class="log-date">${date}</span>  `
    html += `<span style="font-weight: 500; color: ${color}">${log.type}</span>`
    html += `  ---  ${log.message}`;

    let p = document.createElement('p');
    p.innerHTML = html;

    if (log.stack_trace) {
        _insertStackTrace(log.stack_trace, p);
    }

    container.appendChild(p);
}

function _insertStackTrace(stack_trace, container) {
    let throwPaths = stack_trace.split('\n');
    let stackTrace = document.createElement('div');
    
    stackTrace.classList.add('stack-trace');
    stackTrace.innerHTML = `<p>${throwPaths[0]} ...</p>`;

    stackTrace.onclick = function () {
        stackTrace.innerHTML = throwPaths.map(line => `<p>${line}</p>`).join('');
        stackTrace.style.cursor = 'auto';
        stackTrace.onclick = null;
    }
    container.after(stackTrace);
}

function _mockLogs() {
    return [
        { "date": "2023-03-08T15:43:57.708+01:00", "version": "1", "message": "spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning", "path": "org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration$JpaWebConfiguration", "thread": "main", "type": "WARN", "type_value": 30000 },
        { "date": "2023-03-08T15:43:58.319+01:00", "version": "1", "message": "Cannot find template location: classpath:/templates/ (please add some templates or check your Thymeleaf configuration)", "path": "org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration$DefaultTemplateResolverConfiguration", "thread": "main", "type": "WARN", "type_value": 30000 },
        { "date": "2023-03-08T15:43:58.642+01:00", "version": "1", "message": "Tomcat started on port(s): 5000 (http) with context path ''", "path": "org.springframework.boot.web.embedded.tomcat.TomcatWebServer", "thread": "main", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:58.656+01:00", "version": "1", "message": "Started ServerApplication in 5.331 seconds (JVM running for 6.181)", "path": "com.alexeykovzel.fi.ServerApplication", "thread": "main", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:58.718+01:00", "version": "1", "message": "Loading latest form4 refs: start=0, count=100", "path": "com.alexeykovzel.fi.features.secapi.SecLoader", "thread": "scheduling-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:58.946+01:00", "version": "1", "message": "Loading form4 orders: count=47", "path": "com.alexeykovzel.fi.features.stock.insider.SecOrderLoader", "thread": "scheduling-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:59.080+01:00", "version": "1", "message": "Initializing Spring DispatcherServlet 'dispatcherServlet'", "path": "org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/]", "thread": "http-nio-5000-exec-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:59.080+01:00", "version": "1", "message": "Initializing Servlet 'dispatcherServlet'", "path": "org.springframework.web.servlet.DispatcherServlet", "thread": "http-nio-5000-exec-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:43:59.081+01:00", "version": "1", "message": "Completed initialization in 1 ms", "path": "org.springframework.web.servlet.DispatcherServlet", "thread": "http-nio-5000-exec-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:44:41.288+01:00", "version": "1", "message": "Loading form4 orders: count=48197", "path": "com.alexeykovzel.fi.features.stock.insider.SecOrderLoader", "thread": "Thread-1", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:45:33.079+01:00", "version": "1", "message": "Closing JPA EntityManagerFactory for persistence unit 'default'", "path": "org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean", "thread": "SpringApplicationShutdownHook", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:45:33.081+01:00", "version": "1", "message": "HikariPool-1 - Shutdown initiated...", "path": "com.zaxxer.hikari.HikariDataSource", "thread": "SpringApplicationShutdownHook", "type": "INFO", "type_value": 20000 },
        { "date": "2023-03-08T15:45:33.409+01:00", "version": "1", "message": "HikariPool-1 - Shutdown completed.", "path": "com.zaxxer.hikari.HikariDataSource", "thread": "SpringApplicationShutdownHook", "type": "INFO", "type_value": 20000 },
    ]
}
