# wicoder build code generate
auto generate code basic

<ul>
    <li>
        <p>1. add the dependencies <em>pom.xml</em> </p>
        <pre>
            <code class="hljs language-xml"><span class="hljs-tag">&lt;<span class="hljs-name">dependencies</span>&gt;</span>
                <span class="hljs-tag">&lt;<span class="hljs-name">dependency</span>&gt;</span>
                  <span class="hljs-tag">&lt;<span class="hljs-name">groupId</span>&gt;</span>com.github.basewicoder<span class="hljs-tag">&lt;/<span class="hljs-name">groupId</span>&gt;</span>
                  <span class="hljs-tag">&lt;<span class="hljs-name">artifactId</span>&gt;</span>wicoder<span class="hljs-tag">&lt;/<span class="hljs-name">artifactId</span>&gt;</span>
                  <span class="hljs-tag">&lt;<span class="hljs-name">version</span>&gt;</span>4b03cac714<span class="hljs-tag">&lt;/<span class="hljs-name">version</span>&gt;</span>
                <span class="hljs-tag">&lt;/<span class="hljs-name">dependency</span>&gt;</span>
            <span class="hljs-tag">&lt;/<span class="hljs-name">dependencies</span>&gt;</span>
            </code>
        </pre>
    </li>
</ul>
<ul>
    <li>
        <p>2. add the repository <em>pom.xml</em> </p>
        <pre>
                <code class="hljs language-xml"><span class="hljs-tag">&lt;<span class="hljs-name">repositories</span>&gt;</span>
                    <span class="hljs-tag">&lt;<span class="hljs-name">repository</span>&gt;</span>
                      <span class="hljs-tag">&lt;<span class="hljs-name">id</span>&gt;</span>jitpack.io<span class="hljs-tag">&lt;/<span class="hljs-name">id</span>&gt;</span>
                      <span class="hljs-tag">&lt;<span class="hljs-name">url</span>&gt;</span>https://jitpack.io<span class="hljs-tag">&lt;/<span class="hljs-name">url</span>&gt;</span>
                    <span class="hljs-tag">&lt;/<span class="hljs-name">repository</span>&gt;</span>
                <span class="hljs-tag">&lt;/<span class="hljs-name">repositories</span>&gt;</span>
                </code>
        </pre>
    </li>
</ul>
<ul>
    <li>
        <p>3. Install the package class main.</p>
        <pre><code class="hljs language-shell">$ mvn install</code></pre>
    </li>
</ul>
<ul>
    <li>
        <p>4. the package use.</p>
        <pre><code class="hljs language-shell"> CodeBuilder in = new CodeBuilder(); 
        <br> in.setConfig("com.example.configuration")
        <br> .setApplication("com.example.application", "/v1/")
        <br> .setInfraestructure("com.example.infrastructure")
        <br> .setDomain("com.example.domain")
        <br> .setClassFlex(new Class[] {
        <br>  QuartzJob.class 
        <br> }); 
        </code></pre>
    </li>
</ul>

<ul>
    <li>
        <p>5. json  filter.</p>
        <pre><code class="hljs language-shell">{
        "pq_datatype": "JSON",
        "pq_filter": {
            "mode": "AND",
            "data": []
        },
        "pq_curpage": 0,
        "pq_rpp": 10,
        "pq_sort": [
            {
                "dataIndx": "id",
                "dir": "up"
            }
        ]
    }
        </code></pre>
    </li>
</ul>


