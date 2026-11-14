import { useLocation } from 'react-router-dom';

export default function Placeholder() {
  const { pathname } = useLocation();
  const title = pathname.slice(1).replace(/-/g, ' ') || 'Page';

  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center p-8">
      <h1 className="text-2xl font-semibold capitalize">{title}</h1>
      <p className="text-tube-muted mt-2">Coming soon on SubhuTube</p>
      <p className="text-xs text-tube-muted mt-4">100% Ad-Free</p>
    </div>
  );
}