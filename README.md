# wicoder
auto generador de código básico

agregar el repositorio

<repository>
	<id>jitpack.io</id>
	<url>https://jitpack.io</url>
</repository>
    
agregar la dependencia

<div class="row">
  <div class="col-md-6 col-md-offset-3">
 	<code>
	  <dependency>
		<groupId>com.github.basewicoder</groupId>
		<artifactId>wicoder</artifactId>
		<version>319dd434a3</version>
	   </dependency>
	</code>
  </div>
</div>

<ol>
<li>
<p>Authenticate to GitHub Packages. For more information, see "<a href="#authenticating-to-github-packages">Authenticating to GitHub Packages</a>."</p>
</li>
<li>
<p>Add the package dependencies to the <code>dependencies</code> element of your project <em>pom.xml</em> file, replacing <code>com.example:test</code> with your package.</p>
<pre><code class="hljs language-xml"><span class="hljs-tag">&lt;<span class="hljs-name">dependencies</span>&gt;</span>
  <span class="hljs-tag">&lt;<span class="hljs-name">dependency</span>&gt;</span>
    <span class="hljs-tag">&lt;<span class="hljs-name">groupId</span>&gt;</span>com.example<span class="hljs-tag">&lt;/<span class="hljs-name">groupId</span>&gt;</span>
    <span class="hljs-tag">&lt;<span class="hljs-name">artifactId</span>&gt;</span>test<span class="hljs-tag">&lt;/<span class="hljs-name">artifactId</span>&gt;</span>
    <span class="hljs-tag">&lt;<span class="hljs-name">version</span>&gt;</span>1.0.0-SNAPSHOT<span class="hljs-tag">&lt;/<span class="hljs-name">version</span>&gt;</span>
  <span class="hljs-tag">&lt;/<span class="hljs-name">dependency</span>&gt;</span>
<span class="hljs-tag">&lt;/<span class="hljs-name">dependencies</span>&gt;</span>
</code></pre>
</li>
<li>
<p>Install the package.</p>
<pre><code class="hljs language-shell">$ mvn install</code></pre>
</li>
</ol>
