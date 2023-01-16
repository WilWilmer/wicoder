# wicoder
auto generador de código básico

<ol>
    <li>
        <p>agregar la dependencies <em>pom.xml</em> </p>
        <pre>
            <code class="hljs language-xml"><span class="hljs-tag">&lt;<span class="hljs-name">dependencies</span>&gt;</span>
                <span class="hljs-tag">&lt;<span class="hljs-name">dependency</span>&gt;</span>
                <span class="hljs-tag">&lt;<span class="hljs-name">groupId</span>&gt;</span>com.github.basewicoder<span class="hljs-tag">&lt;/<span class="hljs-name">groupId</span>&gt;</span>
                <span class="hljs-tag">&lt;<span class="hljs-name">artifactId</span>&gt;</span>wicoder<span class="hljs-tag">&lt;/<span class="hljs-name">artifactId</span>&gt;</span>
                <span class="hljs-tag">&lt;<span class="hljs-name">version</span>&gt;</span>319dd434a3<span class="hljs-tag">&lt;/<span class="hljs-name">version</span>&gt;</span>
                <span class="hljs-tag">&lt;/<span class="hljs-name">dependency</span>&gt;</span>
            <span class="hljs-tag">&lt;/<span class="hljs-name">dependencies</span>&gt;</span>
            </code>
        </pre>
    </li>
</ol>
<ol>
    <li>
        <p>agregar la repository <em>pom.xml</em> </p>
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
</ol>
<ol>
    <li>
        <p>Install the package.</p>
        <pre><code class="hljs language-shell">$ mvn install</code></pre>
    </li>
</ol>
