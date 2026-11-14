import { useSearchParams } from 'react-router-dom';
import { searchVideos } from '../data/videos';
import VideoGrid from '../components/VideoGrid';

export default function Search() {
  const [params] = useSearchParams();
  const q = params.get('q') || '';
  const results = searchVideos(q);

  return (
    <div className="p-4 md:p-6">
      <p className="text-sm text-tube-muted mb-4">
        {results.length} results for “{q}”
      </p>
      <VideoGrid videos={results} />
    </div>
  );
}