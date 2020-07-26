const tf = require('@tensorflow/tfjs')
const use = require('@tensorflow-models/universal-sentence-encoder')

const tfModel = {

  /*
  @param inputPhrases: An array of Strings with the first element as the
  main comparison String (typically the Decision Title) and the subsequent strings
  as the child comparisons. ["Main Title", "Option 1", "Option 2"]
  @return results: An array of the semantic similarity scores between the main
  comparison string and the child comparison at index i - 1. [0.452, 0.788] for the
  above input example.

  This function is asynchronous and returns a promise, so make sure to use it with
  either async-await or with the .then(results=>...) notation.

  */
  async getSimilarityScores (inputPhrases) {
    await tf.ready()
    const model = await use.load()
    const embeddings = await model.embed(inputPhrases)
    const results = []
    for (let i = 1; i < inputPhrases.length; i++) {
      const sentenceI = embeddings.slice([0, 0], [1])
      const sentenceJ = embeddings.slice([i, 0], [1])
      const score = sentenceI.matMul(sentenceJ, false, true).dataSync()
      // console.log(inputPhrases[0], inputPhrases[i], score[0])
      results.push(score[0])
    }
    return results
  },

  setModelBackend (backendString) {
    tf.setBackend(backendString)
  }
}

export default tfModel
