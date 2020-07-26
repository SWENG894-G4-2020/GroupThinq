import tf from '@tensorflow/tfjs'
const use = require('@tensorflow-models/universal-sentence-encoder')

const tfModel = {
  async getSimilarityScores (inputPhrases) {
    await tf.ready()
    const model = await use.load()
    const embeddings = await model.embed(inputPhrases)
    for (let i = 1; i < inputPhrases.length; i++) {
      const sentenceI = embeddings.slice([0, 0], [1])
      const sentenceJ = embeddings.slice([i, 0], [1])
      const score = sentenceI.matMul(sentenceJ, false, true).dataSync()
      console.log(inputPhrases[0], inputPhrases[i], score[0])
    }
  }
}

export default tfModel
