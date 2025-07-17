<template>
  <div class="about">
    <div class="about-container">
      <h1 class="about-title">海洋生物知识库</h1>
      <div class="about-content">
        <p class="about-text">致力于构建一个集中的平台，让用户能够便捷地浏览、搜索和获取各类海洋生物知识。</p>
        <p class="about-text">通过我们的平台，您可以随时随地获取您感兴趣的海洋生物内容。</p>
        <p class="about-text">此外，平台还允许用户创建、编辑和组织内容，从而将大量的海洋生物知识集中记录、共享和更新。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AboutView',
}
</script>

<style scoped>
.about {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f8ff;
  padding: 20px;
}

.about-container {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 30px;
  max-width: 800px;
  width: 100%;
}

.about-title {
  color: #0066cc;
  text-align: center;
  margin-bottom: 20px;
  font-size: 2.2em;
}

.about-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 15px;
}

.about-text {
  line-height: 1.6;
  text-align: justify;
  color: #333;
  font-size: 1.1em;
  margin-bottom: 10px;
}
</style>