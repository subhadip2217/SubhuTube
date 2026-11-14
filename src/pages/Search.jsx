import { useSearchParams } from 'react-router-dom';
import { searchVideos } from '../data/videos';
import VideoGrid from '../components/VideoGrid';

export default function Search() {
  const [params] = useSearchParams();
  const q = params.get('q') || '';
  const results = searchVideos(q);

  return (
    <div className="p-4 md:p-6">
      {q ? (
        <>
          <p className="text-sm text-tube-muted mb-4">
            {results.length} result{results.length !== 1 ? 's' : ''} for “{q}”
          </p>
          {results.length > 0 ? (
            <VideoGrid videos={results} />
          ) : (
            <div className="text-center py-16">
              <p className="text-lg font-medium">No results found</p>
              <p className="text-tube-muted mt-1">Try different keywords</p>
            </div>
          )}
        </>
      ) : (
        <div className="text-center py-16">
          <p className="text-lg font-medium">Search SubhuTube</p>
          <p className="text-tube-muted mt-1">Type something in the search bar above</p>
        </div>
      )}
    </div>
  );
}